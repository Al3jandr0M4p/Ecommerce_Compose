package com.clay.ecommerce_compose.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.domain.model.UserSession
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(value = State())
    val state: StateFlow<State> = _state

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.EmailChanged -> _state.value =
                _state.value.copy(email = intent.email)

            is Intent.PasswordChanged -> _state.value =
                _state.value.copy(password = intent.password)

            is Intent.Submit -> login()
        }
    }

    private fun login() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val user = authRepository.signIn(
                email = state.value.email,
                password = state.value.password
            )

            if (user != null) {
                val userSession = GetCurrentUserSessionUseCase(authRepository = authRepository).invoke()
                _state.update {
                    it.copy(
                        isLoading = false,
                        loggedIn = true,
                        currentUser = user.copy(
                            id = userSession?.id,
                            roleName = userSession?.role,
                            businessId = userSession?.businessId
                        ),
                    )
                }

            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Usuario o contraseña incorrectos"
                    )
                }

            }
        }
    }
}
