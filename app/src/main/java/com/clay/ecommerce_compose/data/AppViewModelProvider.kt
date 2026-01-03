package com.clay.ecommerce_compose.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clay.ecommerce_compose.activity.MainViewModel
import com.clay.ecommerce_compose.data.remote.HttpClientKtor
import com.clay.ecommerce_compose.data.remote.SupabaseConfig
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.data.repository.BusinessRepository
import com.clay.ecommerce_compose.data.repository.CartRepository
import com.clay.ecommerce_compose.data.repository.UserRepository
import com.clay.ecommerce_compose.data.repository.WalletRepository
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import com.clay.ecommerce_compose.ui.screens.admin.users.UsersViewModel
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel
import com.clay.ecommerce_compose.ui.screens.client.app_activity.TransactionsViewModel
import com.clay.ecommerce_compose.ui.screens.client.app_activity.WalletViewModel
import com.clay.ecommerce_compose.ui.screens.client.business.ModelViewUserBusiness
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.ui.screens.client.cart.checkout.CheckoutViewModel
import com.clay.ecommerce_compose.ui.screens.client.config.ConfigViewModel
import com.clay.ecommerce_compose.ui.screens.client.home.HomeViewModel
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusinessViewModel
import com.clay.ecommerce_compose.data.repository.OrderRepository

class AppViewModelProvider(private val application: Application) : ViewModelProvider.Factory {
    val supabaseClient = SupabaseConfig.client
    val httpClient = HttpClientKtor.client
    val appContext: Context? = application.applicationContext

    private val authRepository by lazy {
        AuthRepository(supabase = supabaseClient)
    }

    private val businessRepository by lazy {
        BusinessRepository(supabase = supabaseClient, context = appContext)
    }

    private val userRepository by lazy {
        UserRepository(supabase = supabaseClient)
    }

    private val cartRepository by lazy {
        CartRepository(supabase = supabaseClient, httpClient = httpClient)
    }

    private val orderRepository by lazy {
        OrderRepository(supabase = supabaseClient, httpClient = httpClient)
    }

    private val walletRepository by lazy {
        WalletRepository(supabase = supabaseClient)
    }

    private val getCurrentUserSessionUseCase by lazy {
        GetCurrentUserSessionUseCase(authRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(userRepository = userRepository) as T
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
            return ConfigViewModel(
                userRepository = userRepository,
                authRepository = authRepository
            ) as T
        }

        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepository = cartRepository) as T
        }

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(userRepository = userRepository) as T
        }

        if (modelClass.isAssignableFrom(BusinessAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusinessAccountViewModel(
                businessAccountRepository = businessRepository,
                authRepository = authRepository
            ) as T
        }

        if (modelClass.isAssignableFrom(ModelViewUserBusiness::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ModelViewUserBusiness(businessRepository) as T
        }

        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(orderRepository = orderRepository) as T
        }

        if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WalletViewModel(walletRepository = walletRepository) as T
        }

        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(walletRepository = walletRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}