package com.clay.ecommerce_compose.data.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clay.ecommerce_compose.data.remote.SupabaseConfig
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel

object AppViewModelProvider : ViewModelProvider.Factory {
    private val authRepository by lazy {
        AuthRepository(SupabaseConfig.client)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}