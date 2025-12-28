package com.clay.ecommerce_compose.ui.screens.businesess

import BusinessAccountProductState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.data.repository.BusinessRepository
import com.clay.ecommerce_compose.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BusinessAccountViewModel(
    private val businessAccountRepository: BusinessRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _businessProfile = MutableStateFlow<BusinessProfile?>(value = null)
    val businessProfile: StateFlow<BusinessProfile?> = _businessProfile.asStateFlow()

    private val _businessProduct = MutableStateFlow<List<ProductPayload>?>(value = null)
    val businessProduct: StateFlow<List<ProductPayload>?> = _businessProduct.asStateFlow()

    private val _businessState = MutableStateFlow(BusinessAccountProductState())
    val state: StateFlow<BusinessAccountProductState> = _businessState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvent: SharedFlow<UIEvents> = _uiEvents.asSharedFlow()

    private val _categories = MutableStateFlow<List<ProductCategory>>(value = emptyList())
    val categories: StateFlow<List<ProductCategory>> = _categories.asStateFlow()

    private val _productsByCategory = MutableStateFlow<List<ProductsByCategory>>(value = emptyList())
    val productsByCategory: StateFlow<List<ProductsByCategory>> = _productsByCategory.asStateFlow()

    private val _lowStockProducts = MutableStateFlow<List<LowStockProduct>>(value = emptyList())
    val lowStockProducts: StateFlow<List<LowStockProduct>> = _lowStockProducts.asStateFlow()

    private val _stockMovements = MutableStateFlow<List<StockMovement>>(value = emptyList())
    val stockMovements: StateFlow<List<StockMovement>> = _stockMovements.asStateFlow()

    private val _selectedProductForRestock = MutableStateFlow<Int?>(value = null)
    val selectedProductForRestock: StateFlow<Int?> = _selectedProductForRestock.asStateFlow()

    private val _productStockState = MutableStateFlow(value = StockManagementState())
    val productStockState: StateFlow<StockManagementState> = _productStockState.asStateFlow()

    fun loadBusinessById(businessId: String) {
        viewModelScope.launch {
            try {
                val result = businessAccountRepository.getBusinessById(businessId)
                _businessProfile.value = result

                result?.let { profile ->
                    val businessIntId = _businessState.value.businessId.toIntOrNull()
                    loadProductsBusinessById(businessId = businessIntId)

                    // Cargar datos de stock management
                    businessIntId?.let { id ->
                        loadCategories(businessId = id)
                        loadProductsByCategory(businessId = id)
                        loadLowStockProducts(businessId = id)
                    }
                }
            } catch (e: Exception) {
                Log.e("BusinessAccountViewModel", "Error al cargar el perfil del negocio ${e.message}")
            }
        }
    }

    fun loadProductsBusinessById(businessId: Int?) {
        viewModelScope.launch {
            try {
                if (businessId == null) return@launch
                val products = businessAccountRepository.getProductsByBusinessId(businessId)
                _businessProduct.value = products
            } catch (e: Exception) {
                Log.e("BusinessAccountViewModel", "Error al cargar los productos ${e.message}")
            }
        }
    }

    fun handleIntent(intent: BusinessAccountProductIntent) {
        val current = _businessState.value
        when (intent) {
            is BusinessAccountProductIntent.SetBusinessId -> {
                _businessState.value = current.copy(businessId = intent.id)
                val businessIntId = intent.id.toIntOrNull()
                loadProductsBusinessById(businessId = businessIntId)

                // Cargar datos adicionales
                businessIntId?.let { id ->
                    loadCategories(id)
                    loadProductsByCategory(id)
                    loadLowStockProducts(id)
                }
            }

            is BusinessAccountProductIntent.ProductName ->
                _businessState.value = current.copy(name = intent.name, nameError = null)

            is BusinessAccountProductIntent.ProductImage ->
                uploadImageToBucket(localUri = intent.imgUrl)

            is BusinessAccountProductIntent.ProductDescription ->
                _businessState.value = current.copy(description = intent.description)

            is BusinessAccountProductIntent.ProductPrice ->
                _businessState.value = current.copy(price = intent.price, priceError = null)

            is BusinessAccountProductIntent.ProductStock ->
                _businessState.value = current.copy(stock = intent.stock, stockError = null)

            // Nuevos intents para categorías y stock
            is BusinessAccountProductIntent.SetCategory ->
                _businessState.value = current.copy(categoryId = intent.categoryId)

            is BusinessAccountProductIntent.SetMinStock ->
                _businessState.value = current.copy(minStock = intent.minStock)

            is BusinessAccountProductIntent.SetMaxStock ->
                _businessState.value = current.copy(maxStock = intent.maxStock)

            is BusinessAccountProductIntent.SetStockAlertEnabled ->
                _businessState.value = current.copy(stockAlertEnabled = intent.enabled)

            is BusinessAccountProductIntent.ProductActive ->
                _businessState.value = current.copy(isActive = true)

            is BusinessAccountProductIntent.ProductInactive ->
                _businessState.value = current.copy(isActive = false)

            is BusinessAccountProductIntent.DisableProduct -> disableProduct()
            is BusinessAccountProductIntent.AddProduct -> addProduct()
            is BusinessAccountProductIntent.UpdateProduct -> updateProduct()
        }
    }

    private fun uploadImageToBucket(localUri: String) {
        viewModelScope.launch {
            _uiEvents.emit(UIEvents.ShowMessage("Subiendo imagen...."))

            try {
                val result = businessAccountRepository.uploadProductImage(localUri)

                if (result.isSuccess) {
                    val publicUrl = result.getOrNull()
                    if (publicUrl != null) {
                        _businessState.value = _businessState.value.copy(imgUrl = publicUrl)
                        _uiEvents.emit(UIEvents.ShowMessage("Imagen subida"))
                        Log.d("BusinessViewModel", "Imagen subida exitosamente $publicUrl")
                    }
                } else {
                    _uiEvents.emit(UIEvents.ShowMessage("Error al subir imagen ${result.exceptionOrNull()?.message}"))
                }
            } catch (e: Exception) {
                Log.e("BusinessViewModel", "Error al subir imagen ${e.message}")
                _uiEvents.emit(UIEvents.ShowMessage("Error al subir imagen ${e.message}"))
            }
        }
    }

    private fun validate(state: BusinessAccountProductState): Pair<Boolean, BusinessAccountProductState> {
        var ok = true
        var s = state

        if (s.name.isBlank()) {
            s = s.copy(nameError = "Nombre requerido")
            ok = false
        }

        val priceVal = s.price.toDoubleOrNull()
        if (s.price.isBlank() || priceVal == null || priceVal < 0.0) {
            s = s.copy(priceError = "Precio inválido")
            ok = false
        }

        if (s.hasStockControl) {
            val stockInt = s.stock.toIntOrNull()
            when {
                s.stock.isBlank() -> {
                    s = s.copy(stockError = "Ingrese el stock")
                    ok = false
                }
                stockInt == null -> {
                    s = s.copy(stockError = "Solo números")
                    ok = false
                }
                stockInt < 0 -> {
                    s = s.copy(stockError = "No puede ser negativo")
                    ok = false
                }
            }
        }

        return Pair(ok, s)
    }

    private fun disableProduct() {
        val current = _businessState.value
        viewModelScope.launch {
            val productId = current.id
            if (productId == null) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto no seleccionado"))
                return@launch
            }

            val result = businessAccountRepository.disableProduct(productId)
            if (result.isSuccess) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto desactivado"))

                val businessIntId = current.businessId.toIntOrNull()
                businessIntId?.let { id ->
                    loadProductsBusinessById(id)
                    loadProductsByCategory(id)
                }
            } else {
                _uiEvents.emit(UIEvents.ShowMessage("Error ${result.exceptionOrNull()?.message ?: "desconocido"}"))
            }
        }
    }

    private fun addProduct() {
        val current = _businessState.value

        Log.d("DEBUG", "==== addProduct ====")
        Log.d("DEBUG", "ID: ${current.id}")
        Log.d("DEBUG", "Nombre: ${current.name}")
        Log.d("DEBUG", "Precio: ${current.price}")
        Log.d("DEBUG", "Stock: ${current.stock}")
        Log.d("DEBUG", "BusinessId: ${current.businessId}")
        Log.d("DEBUG", "CategoryId: ${current.categoryId}")

        viewModelScope.launch {
            val (ok, validatedState) = validate(current)
            _businessState.value = validatedState

            if (validatedState.businessId.isBlank()) {
                _uiEvents.emit(UIEvents.ShowMessage("Business ID faltante"))
                return@launch
            }

            val businessIntId = validatedState.businessId.toIntOrNull()
            if (businessIntId == null) {
                _uiEvents.emit(UIEvents.ShowMessage("Business ID inválido"))
                return@launch
            }

            if (!ok) {
                _uiEvents.emit(UIEvents.ShowMessage("Corrige los errores"))
                return@launch
            }

            val stockInt = if (validatedState.hasStockControl) {
                validatedState.stock.toIntOrNull() ?: 0
            } else {
                0 // Sin control de stock
            }

            val payload = ProductInsertPayload(
                name = validatedState.name,
                description = validatedState.description,
                price = validatedState.price.toDoubleOrNull() ?: 0.0,
                isActive = validatedState.isActive,
                imageUrl = validatedState.imgUrl.ifBlank { null },
                stock = stockInt,
                businessId = businessIntId,
                categoryId = validatedState.categoryId,
                minStock = validatedState.minStock,
                maxStock = validatedState.maxStock,
                stockAlertEnabled = validatedState.stockAlertEnabled
            )

            Log.d("PAYLOAD", "Stock enviado: $stockInt")

            val result = businessAccountRepository.addProduct(payload)
            if (result.isSuccess) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto creado"))
                _uiEvents.emit(UIEvents.CloseSheet)

                loadProductsBusinessById(businessId = businessIntId)
                loadProductsByCategory(businessIntId)
                loadLowStockProducts(businessIntId)

                // Resetear estado
                _businessState.value = BusinessAccountProductState(
                    businessId = validatedState.businessId
                )
            } else {
                _uiEvents.emit(UIEvents.ShowMessage("Error: ${result.exceptionOrNull()?.message ?: "desconocido"}"))
            }
        }
    }

    private fun updateProduct() {
        val current = _businessState.value
        viewModelScope.launch {
            val (ok, validatedState) = validate(current)
            _businessState.value = validatedState

            val businessIntId = validatedState.businessId.toIntOrNull()
            if (businessIntId == null) {
                _uiEvents.emit(UIEvents.ShowMessage("ID de negocio inválido"))
                return@launch
            }

            if (!ok) {
                _uiEvents.emit(UIEvents.ShowMessage("Corrige los errores"))
                return@launch
            }

            val productId = validatedState.id
            if (productId == null) {
                _uiEvents.emit(UIEvents.ShowMessage("ID de producto inválido"))
                return@launch
            }

            val stockInt = if (validatedState.hasStockControl) {
                validatedState.stock.toIntOrNull() ?: 0
            } else {
                0
            }

            val updates = ProductPayload(
                id = productId,
                name = validatedState.name,
                description = validatedState.description,
                price = validatedState.price.toDoubleOrNull() ?: 0.0,
                isActive = validatedState.isActive,
                imageUrl = validatedState.imgUrl.ifBlank { null },
                stock = stockInt,
                businessId = businessIntId,
                categoryId = validatedState.categoryId,
                minStock = validatedState.minStock,
                maxStock = validatedState.maxStock,
                stockAlertEnabled = validatedState.stockAlertEnabled
            )

            val result = businessAccountRepository.updateProduct(productId, updates)
            if (result.isSuccess) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto actualizado"))
                _uiEvents.emit(UIEvents.CloseSheet)

                loadProductsBusinessById(businessIntId)
                loadProductsByCategory(businessIntId)
                loadLowStockProducts(businessIntId)
            } else {
                _uiEvents.emit(UIEvents.ShowMessage("Error: ${result.exceptionOrNull()?.message ?: "desconocido"}"))
            }
        }
    }

    fun loadCategories(businessId: Int) {
        viewModelScope.launch {
            _productStockState.value = _productStockState.value.copy(isLoading = true)
            try {
                val result = businessAccountRepository.getCategories(businessId)
                if (result.isSuccess) {
                    val categories = result.getOrNull() ?: emptyList()
                    _categories.value = categories
                    _productStockState.value = _productStockState.value.copy(
                        categories = categories,
                        isLoading = false,
                        error = null
                    )
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                    _productStockState.value = _productStockState.value.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                    Log.e("BusinessAccountViewModel", "Error al cargar categorías: $errorMsg")
                }
            } catch (e: Exception) {
                _productStockState.value = _productStockState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                Log.e("BusinessAccountViewModel", "Error al cargar categorías: ${e.message}")
            }
        }
    }

    fun createCategory(name: String, description: String?, businessId: Int) {
        viewModelScope.launch {
            try {
                val categoryRequest = CategoryRequest(
                    businessId = businessId,
                    name = name,
                    description = description
                )
                val result = businessAccountRepository.createCategory(categoryRequest)
                if (result.isSuccess) {
                    _uiEvents.emit(UIEvents.ShowMessage("Categoría creada"))
                    loadCategories(businessId)
                } else {
                    _uiEvents.emit(UIEvents.ShowMessage("Error: ${result.exceptionOrNull()?.message}"))
                }
            } catch (e: Exception) {
                Log.e("BusinessAccountViewModel", "Error al crear categoría: ${e.message}")
                _uiEvents.emit(UIEvents.ShowMessage("Error: ${e.message}"))
            }
        }
    }

    fun loadProductsByCategory(businessId: Int) {
        viewModelScope.launch {
            _productStockState.value = _productStockState.value.copy(isLoading = true)
            try {
                val result = businessAccountRepository.getProductsByCategory(businessId)
                if (result.isSuccess) {
                    val products = result.getOrNull() ?: emptyList()
                    _productsByCategory.value = products
                    _productStockState.value = _productStockState.value.copy(
                        productsByCategory = products,
                        isLoading = false,
                        error = null
                    )
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                    _productStockState.value = _productStockState.value.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                    Log.e("BusinessAccountViewModel", "Error al cargar productos por categoría: $errorMsg")
                }
            } catch (e: Exception) {
                _productStockState.value = _productStockState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                Log.e("BusinessAccountViewModel", "Error al cargar productos por categoría: ${e.message}")
            }
        }
    }

    // ===== MÉTODOS PARA STOCK BAJO =====

    fun loadLowStockProducts(businessId: Int) {
        viewModelScope.launch {
            try {
                val result = businessAccountRepository.getLowStockProducts(businessId)
                if (result.isSuccess) {
                    val lowStock = result.getOrNull() ?: emptyList()
                    _lowStockProducts.value = lowStock
                    _productStockState.value = _productStockState.value.copy(
                        lowStockProducts = lowStock,
                        showLowStockAlert = lowStock.isNotEmpty()
                    )
                } else {
                    Log.e("BusinessAccountViewModel", "Error al cargar productos con stock bajo: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("BusinessAccountViewModel", "Error al cargar productos con stock bajo: ${e.message}")
            }
        }
    }

    fun loadStockMovements(productId: Int) {
        viewModelScope.launch {
            try {
                val result = businessAccountRepository.getStockMovements(productId)
                if (result.isSuccess) {
                    _stockMovements.value = result.getOrNull() ?: emptyList()
                } else {
                    Log.e("BusinessAccountViewModel", "Error al cargar movimientos: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("BusinessAccountViewModel", "Error al cargar movimientos: ${e.message}")
            }
        }
    }

    fun adjustStock(
        productId: Int,
        quantity: Int,
        movementType: StockMovementType,
        notes: String?
    ) {
        viewModelScope.launch {
            try {
                val adjustment = StockAdjustmentRequest(
                    productId = productId,
                    quantity = quantity,
                    movementType = movementType,
                    notes = notes
                )

                val result = businessAccountRepository.adjustStock(adjustment)
                if (result.isSuccess) {
                    _uiEvents.emit(UIEvents.ShowMessage("Stock actualizado"))

                    // Recargar datos
                    val businessIntId = _businessState.value.businessId.toIntOrNull()
                    businessIntId?.let { id ->
                        loadProductsBusinessById(id)
                        loadProductsByCategory(id)
                        loadLowStockProducts(id)
                        loadStockMovements(productId)
                    }

                    _selectedProductForRestock.value = null
                } else {
                    _uiEvents.emit(UIEvents.ShowMessage("Error: ${result.exceptionOrNull()?.message}"))
                }
            } catch (e: Exception) {
                Log.e("BusinessAccountViewModel", "Error al ajustar stock: ${e.message}")
                _uiEvents.emit(UIEvents.ShowMessage("Error: ${e.message}"))
            }
        }
    }

    fun openRestockDialog(productId: Int) {
        _selectedProductForRestock.value = productId
    }

    fun closeRestockDialog() {
        _selectedProductForRestock.value = null
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
