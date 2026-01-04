package com.clay.ecommerce_compose.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(
    private val supabase: SupabaseClient,
    private val getCurrentUserSessionUseCase: GetCurrentUserSessionUseCase,
) : ViewModel() {
    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("MainViewModel", "INIT -> SPLASHSTATE = Loading")
            supabase.auth.loadFromStorage()

            Log.d("MainViewModel", "Loading session from supabase")
            getCurrentUserSessionUseCase.loadSession()

            delay(100)

            val session = getCurrentUserSessionUseCase.session.first()
            Log.d("MainViewModel", "session result = $session")

            _splashState.value = if (session == null) {
                Log.d("MainViewModel", "User not authenticated")
                SplashState.NotAuthenticated
            } else {
                Log.d("MainViewModel", "User authenticated -> role=${session.role}")
                SplashState.Authenticated(session)
            }
        }
    }
}
