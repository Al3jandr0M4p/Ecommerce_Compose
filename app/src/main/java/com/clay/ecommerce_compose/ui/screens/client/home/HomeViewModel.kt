package com.clay.ecommerce_compose.ui.screens.client.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.data.repository.UserRepository
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _businessProfile = MutableStateFlow<List<BusinessProfile?>>(value = emptyList())
    val businessState: StateFlow<List<BusinessProfile?>> = _businessProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var hasLoaded = false

    fun loadBusiness(force: Boolean = false) {
        if (hasLoaded && !force) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = userRepository.getAllBusiness()
                _businessProfile.value = result
                Log.d("HomeViewModel", "Los negocios se cargaron correctamente $result")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al cargar Los negocios ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}
