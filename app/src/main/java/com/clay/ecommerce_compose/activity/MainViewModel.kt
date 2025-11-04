package com.clay.ecommerce_compose.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val supabase: SupabaseClient,
    private val getCurrentUserSessionUseCase: GetCurrentUserSessionUseCase
) : ViewModel() {
    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState

    init {
        viewModelScope.launch {
            supabase.auth.loadFromStorage()
            checkUserSession()
        }
    }

    private suspend fun checkUserSession() {
        val session = getCurrentUserSessionUseCase()
        _splashState.value = SplashState.Success(session)
    }
}
