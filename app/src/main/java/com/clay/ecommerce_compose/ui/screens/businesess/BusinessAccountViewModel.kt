package com.clay.ecommerce_compose.ui.screens.businesess

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.BusinessRepository
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessAccountViewModel(private val businessAccountRepository: BusinessRepository) :
    ViewModel() {

    private val _businessProfile = MutableStateFlow<BusinessProfile?>(null)
    val businessProfile: StateFlow<BusinessProfile?> = _businessProfile

    private val _businessState = MutableStateFlow(value = BusinessAccountProductState())
    val state: StateFlow<BusinessAccountProductState> = _businessState

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvents: SharedFlow<UIEvents> = _uiEvents

    fun loadBusinessById(businessId: String) {
        viewModelScope.launch {
            try {
                val result = businessAccountRepository.getBusinessById(businessId)
                _businessProfile.value = result
            } catch (e: Exception) {
                Log.e(
                    "BusinessAccountViewModel",
                    "Error al cargar el perfil del negocio ${e.message}"
                )
            }
        }
    }

    fun handleIntent(intent: BusinessAccountProductIntent) {
        val current = _businessState.value
        when (intent) {
            is BusinessAccountProductIntent.SetBusinessId ->
                _businessState.value = current.copy(businessId = intent.id)

            is BusinessAccountProductIntent.ProductName -> _businessState.value =
                current.copy(name = intent.name, nameError = null)

            is BusinessAccountProductIntent.ProductImage -> _businessState.value =
                current.copy(imgUrl = intent.imgUrl)

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
            if (result.isSuccess) _uiEvents.emit(UIEvents.ShowMessage("Producto desactivado"))
            else _uiEvents.emit(UIEvents.ShowMessage("Error ${result.exceptionOrNull()?.message ?: "desconocido"}"))
        }
    }

    private
    fun addProduct() {
        val current = _businessState.value

        Log.d("DEBUG", "== addProduct ==")
        Log.d("DEBUG", "Nombre: ${current.name}")
        Log.d("DEBUG", "Precio: ${current.price}")
        Log.d("DEBUG", "Stock: ${current.stock}")
        Log.d("DEBUG", "BusinessId: ${current.businessId}")
        Log.d("DEBUG", "Imagen: ${current.imgUrl}")

        viewModelScope.launch {
            val (ok, validatedState) = validate(current)
            _businessState.value = validatedState

            if (!ok) {
                _uiEvents.emit(UIEvents.ShowMessage("Corrige los errores"))
                return@launch
            }

            val payload = mutableMapOf<String, Any?>(
                "name" to validatedState.name,
                "description" to validatedState.description,
                "price" to validatedState.price.toDoubleOrNull(),
                "is_active" to validatedState.isActive,
                "image_url" to validatedState.imgUrl
            )

            if (validatedState.hasStockControl) {
                payload["stock"] = validatedState.stock.toIntOrNull() ?: 0
            } else {
                payload["stock"] = null
            }

            if (validatedState.businessId.isBlank()) {
                _uiEvents.emit(UIEvents.ShowMessage("Business ID faltante"))
                return@launch
            }

            val result = businessAccountRepository.addProduct(validatedState.businessId, payload)
            if (result.isSuccess) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto creado"))
                _uiEvents.emit(UIEvents.CloseSheet)

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
            if (!ok) {
                _uiEvents.emit(UIEvents.ShowMessage("Corrige los errores"))
                return@launch
            }

            val productId = validatedState.id
            if (productId == null) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto no seleccionado para actualizar"))
                return@launch
            }

            val updates = mutableMapOf<String, Any?>(
                "name" to validatedState.name,
                "description" to validatedState.description,
                "price" to validatedState.price.toDoubleOrNull(),
                "is_active" to validatedState.isActive,
                "image_url" to validatedState.imgUrl
            )

            if (validatedState.hasStockControl) {
                updates["stock"] = validatedState.stock.toIntOrNull() ?: 0
            } else {
                updates["stock"] = null
            }

            val result = businessAccountRepository.updateProduct(productId, updates)
            if (result.isSuccess) {
                _uiEvents.emit(UIEvents.ShowMessage("Producto actualizado"))
                _uiEvents.emit(UIEvents.CloseSheet)
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
