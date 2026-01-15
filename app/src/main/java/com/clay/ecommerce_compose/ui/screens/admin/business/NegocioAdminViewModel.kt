package com.clay.ecommerce_compose.ui.screens.admin.negocios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.NegocioRepository
import com.clay.ecommerce_compose.domain.model.Business
import com.clay.ecommerce_compose.domain.model.UserWithRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NegocioAdminViewModel(
    private val repository: NegocioRepository
) : ViewModel() {

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses: StateFlow<List<Business>> = _businesses

    private val _businessUsers = MutableStateFlow<List<UserWithRole>>(emptyList())
    val businessUsers: StateFlow<List<UserWithRole>> = _businessUsers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadBusinesses()
        loadBusinessUsers()
    }

    fun loadBusinesses() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _businesses.value = repository.getBusinesses()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar negocios: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBusinessUsers() {
        viewModelScope.launch {
            try {
                _businessUsers.value = repository.getUsersWithBusinessRole()
            } catch (e: Exception) {
                _error.value = "Error al cargar usuarios: ${e.message}"
            }
        }
    }

    fun createBusiness(
        ownerId: String,
        name: String,
        phone: String,
        category: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.createBusiness(
                    ownerId = ownerId,
                    name = name,
                    phone = phone,
                    category = category
                )
                loadBusinesses()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al crear negocio: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateBusiness(id: Int, name: String, phone: String, category: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.updateBusiness(id, name, phone, category)
                loadBusinesses()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al actualizar negocio: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteBusiness(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteBusiness(id)
                loadBusinesses()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al eliminar negocio: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}