package com.clay.ecommerce_compose.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clay.ecommerce_compose.ui.screens.client.config.ConfigViewModel
import com.clay.ecommerce_compose.activity.MainViewModel
import com.clay.ecommerce_compose.data.remote.SupabaseConfig
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.data.repository.BusinessRepository
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusinessViewModel

class AppViewModelProvider(private val application: Application) : ViewModelProvider.Factory {
    private val authRepository by lazy {
        AuthRepository(SupabaseConfig.client)
    }

    private val businessRepository by lazy {
        BusinessRepository(SupabaseConfig.client)
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
                getCurrentUserSessionUseCase = getCurrentUserSessionUseCase,
            ) as T
        }

        if (modelClass.isAssignableFrom(RegisterBusinessViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterBusinessViewModel(authRepository, application) as T
        }

        if (modelClass.isAssignableFrom(ConfigViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfigViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(BusinessAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusinessAccountViewModel(businessRepository, authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
