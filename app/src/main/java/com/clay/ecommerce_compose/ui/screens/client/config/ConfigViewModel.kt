package com.clay.ecommerce_compose.ui.screens.client.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import kotlinx.coroutines.launch

class ConfigViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
