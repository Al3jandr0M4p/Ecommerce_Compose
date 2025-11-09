package com.clay.ecommerce_compose.ui.screens.businesess

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.data.repository.BusinessRepository
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessAccountViewModel(
    private val businessAccountRepository: BusinessRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _businessProfile = MutableStateFlow<BusinessProfile?>(null)
    val businessProfile: StateFlow<BusinessProfile?> = _businessProfile

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

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
