package com.clay.ecommerce_compose.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.domain.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val loggedIn: Boolean = false,
        val currentUser: Profile? = null
    )

    sealed class Intent {
        data class EmailChanged(val email: String) : Intent()
        data class PasswordChanged(val password: String) : Intent()
        object Submit : Intent()
    }

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

    private fun login() {
        val current = _state.value
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val user = authRepository.signIn(
                email = current.email,
                password = current.password
            )

            if (user != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        loggedIn = true,
                        currentUser = user
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
