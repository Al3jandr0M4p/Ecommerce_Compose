package com.clay.ecommerce_compose.ui.screens.businesess

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.BusinessRepository
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.ProductPayload
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessAccountViewModel(private val businessAccountRepository: BusinessRepository) :
    ViewModel() {

    private val _businessProfile = MutableStateFlow<BusinessProfile?>(null)
    val businessProfile: StateFlow<BusinessProfile?> = _businessProfile

    private val _businessProduct = MutableStateFlow<List<ProductPayload>?>(null)
    val businessProduct: StateFlow<List<ProductPayload>?> = _businessProduct

    private val _businessState = MutableStateFlow(value = BusinessAccountProductState())
    val state: StateFlow<BusinessAccountProductState> = _businessState

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvent: SharedFlow<UIEvents> = _uiEvents

    fun loadBusinessById(businessId: String) {
        viewModelScope.launch {
            try {
                val result = businessAccountRepository.getBusinessById(businessId)
                _businessProfile.value = result

                result?.let { profile ->
                    val businessIntId = _businessState.value.businessId.toIntOrNull()
                    loadProductsBusinessById( businessIntId)
                }
            } catch (e: Exception) {
                Log.e(
                    "BusinessAccountViewModel",
                    "Error al cargar el perfil del negocio ${e.message}"
                )
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
                loadProductsBusinessById(intent.id.toIntOrNull())
            }

            is BusinessAccountProductIntent.ProductName -> _businessState.value =
                current.copy(name = intent.name, nameError = null)

            is BusinessAccountProductIntent.ProductImage -> {
                uploadImageToBucket(intent.imgUrl)
            }

            is BusinessAccountProductIntent.ProductDescription -> _businessState.value =
                current.copy(description = intent.description)

            is BusinessAccountProductIntent.ProductPrice -> _businessState.value =
                current.copy(price = intent.price, priceError = null)

            is BusinessAccountProductIntent.ProductStock -> _businessState.value =
                current.copy(stock = intent.stock, stockError = null)

            is BusinessAccountProductIntent.SetProductStockControl -> _businessState.value =
                current.copy(
                    hasStockControl = intent.has
                )

            is BusinessAccountProductIntent.ProductActive -> _businessState.value =
                current.copy(isActive = true)

            is BusinessAccountProductIntent.ProductInactive -> _businessState.value =
                current.copy(isActive = false)

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
                        _businessState.value = _businessState.value.copy(imgUrl = publicUrl.toString())
                        _uiEvents.emit(UIEvents.ShowMessage("Imagen subida"))
                        Log.d("BusinessViewModel", "Imagen subida exitosamente $publicUrl")
                    }
                } else {
                    _uiEvents.emit(UIEvents.ShowMessage("Error al subir imagen ${result.exceptionOrNull()?.message}"));
                }
            } catch (e: Exception) {
                Log.e("BusinessViewModel", "Error al subir imagen ${e.message}")
                _uiEvents.emit(UIEvents.ShowMessage("Error al subir imagen ${e.message}"));
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
            s = s.copy(priceError = "Precio invalido")
            ok = false
        }

        if (s.hasStockControl) {
            val stockVal = s.stock.toIntOrNull()
            if (s.stock.isBlank() || stockVal == null || stockVal < 0) {
                s = s.copy(stockError = "Stock invalido")
                ok = false
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
                loadProductsBusinessById(current.id)
            }
            else _uiEvents.emit(UIEvents.ShowMessage("Error ${result.exceptionOrNull()?.message ?: "desconocido"}"))
        }
    }

    private
    fun addProduct() {
        val current = _businessState.value

        Log.d("DEBUG", "==== addProduct ====")
        Log.d("DEBUG", "ID: ${current.id}")
        Log.d("DEBUG", "Nombre: ${current.name}")
        Log.d("DEBUG", "Precio: ${current.price}")
        Log.d("DEBUG", "Stock: ${current.stock}")
        Log.d("DEBUG", "BusinessId: ${current.businessId}")
        Log.d("DEBUG", "Imagen: ${current.imgUrl}")

        viewModelScope.launch {
            val (ok, validatedState) = validate(current)
            _businessState.value = validatedState

            val businessIntId = validatedState.id
            if (businessIntId == null) {
                _uiEvents.emit(UIEvents.ShowMessage("Business ID faltante"))
                return@launch
            }

            if (!ok) {
                _uiEvents.emit(UIEvents.ShowMessage("Corrige los errores"))
                return@launch
            }

            val stockValue = if (validatedState.hasStockControl) {
                validatedState.stock.toIntOrNull() ?: 0
            } else {
                null
            }

            val payload = ProductPayload(
                name = validatedState.name,
                description = validatedState.description,
                price = validatedState.price.toDoubleOrNull() ?: 0.0,
                isActive = validatedState.isActive,
                imageUrl = validatedState.imgUrl,
                stock = stockValue,
                businessId = validatedState.businessId
            )

            if (validatedState.businessId.isBlank()) {
                _uiEvents.emit(UIEvents.ShowMessage("Business ID faltante"))
                return@launch
            }

            val result = businessAccountRepository.addProduct(payload)
            if (result.isSuccess) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto creado"))
                _uiEvents.emit(UIEvents.CloseSheet)

                loadProductsBusinessById(businessIntId)

                _businessState.value =
                    BusinessAccountProductState(businessId = validatedState.businessId)
            } else {
                _uiEvents.emit(UIEvents.ShowMessage("Erro ${result.exceptionOrNull()?.message ?: "desconocido"}"))
            }
        }
    }

    private fun updateProduct() {
        val current = _businessState.value
        viewModelScope.launch {
            val (ok, validatedState) = validate(current)
            _businessState.value = validatedState

            val businessIntId = validatedState.id
            if (businessIntId == null) {
                _uiEvents.emit(UIEvents.ShowMessage("ID de negocio inválido"))
                return@launch
            }

            if (!ok) {
                _uiEvents.emit(UIEvents.ShowMessage("Corrige los errores"))
                return@launch
            }

            val productId = validatedState.id

            val stockValue = if (validatedState.hasStockControl) {
                validatedState.stock.toIntOrNull() ?: 0
            } else {
                null
            }

            val updates = ProductPayload(
                name = validatedState.name,
                description = validatedState.description,
                price = validatedState.price.toDoubleOrNull() ?: 0.0,
                isActive = validatedState.isActive,
                imageUrl = validatedState.imgUrl,
                stock = stockValue,
                businessId = validatedState.businessId
            )

            val result = businessAccountRepository.updateProduct(productId, updates)
            if (result.isSuccess) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto actualizado"))
                _uiEvents.emit(UIEvents.CloseSheet)

                loadProductsBusinessById(businessIntId)
            } else {
                _uiEvents.emit(UIEvents.ShowMessage("Error: ${result.exceptionOrNull()?.message ?: "desconocido"}"))
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            businessAccountRepository.signOut()
        }
    }
}
