package com.clay.ecommerce_compose.ui.screens.admin.delivery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.DeliveryRepository
import com.clay.ecommerce_compose.domain.model.Delivery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeliveryViewModel(
    private val deliveryRepository: DeliveryRepository
) : ViewModel() {

    private val _deliveries = MutableStateFlow<List<Delivery>>(emptyList())
    val deliveries: StateFlow<List<Delivery>> = _deliveries

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadDeliveries()
    }

    fun loadDeliveries() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _deliveries.value = deliveryRepository.getAllDeliveries()
                Log.d("DeliveryViewModel", "Deliveries cargados: ${_deliveries.value.size}")
                _error.value = null
            } catch (e: Exception) {
                Log.e("DeliveryViewModel", "Error al cargar deliveries", e)
                _error.value = "Error al cargar deliveries: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createDelivery(
        customerName: String,
        deliveryPerson: String,
        phone: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deliveryRepository.createDelivery(
                    customerName,
                    deliveryPerson,
                    phone
                )
                loadDeliveries()
                _error.value = null
            } catch (e: Exception) {
                Log.e("DeliveryViewModel", "Error al crear delivery", e)
                _error.value = "Error al crear delivery: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateDelivery(
        id: String,
        customerName: String,
        deliveryPerson: String,
        phone: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deliveryRepository.updateDelivery(
                    id,
                    customerName,
                    deliveryPerson,
                    phone
                )
                loadDeliveries()
                _error.value = null
            } catch (e: Exception) {
                Log.e("DeliveryViewModel", "Error al actualizar delivery", e)
                _error.value = "Error al actualizar delivery: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteDelivery(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deliveryRepository.deleteDelivery(id)
                loadDeliveries()
                _error.value = null
            } catch (e: Exception) {
                Log.e("DeliveryViewModel", "Error al eliminar delivery", e)
                _error.value = "Error al eliminar delivery: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}