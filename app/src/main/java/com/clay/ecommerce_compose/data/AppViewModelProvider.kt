package com.clay.ecommerce_compose.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clay.ecommerce_compose.ui.screens.signOut.ConfigViewModel
import com.clay.ecommerce_compose.MainViewModel
import com.clay.ecommerce_compose.data.remote.SupabaseConfig
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.BusinessViewModel

class AppViewModelProvider(private val application: Application) : ViewModelProvider.Factory {
    private val authRepository by lazy {
        AuthRepository(SupabaseConfig.client)
    }

    private val getCurrentUserSessionUseCase by lazy {
        GetCurrentUserSessionUseCase(authRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                supabase = SupabaseConfig.client,
                getCurrentUserSessionUseCase = getCurrentUserSessionUseCase
            ) as T
        }

        if (modelClass.isAssignableFrom(BusinessViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusinessViewModel(authRepository, application) as T
        }

        if (modelClass.isAssignableFrom(ConfigViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfigViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
