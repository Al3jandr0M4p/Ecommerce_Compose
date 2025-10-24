package com.clay.ecommerce_compose.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.data.repository.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    data class State(
        val email: String = "",
        val password: String = "",
        val username: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val registered: Boolean = false,
        val currentUser: Profile? = null
    )

    sealed class Intent {
        data class EmailChanged(val email: String) : Intent()
        data class UsernameChanged(val username: String) : Intent()
        data class PasswordChanged(val password: String) : Intent()
        object Submit : Intent()
    }

    private val _state = MutableStateFlow(value = State())
    val state: StateFlow<State> = _state.asStateFlow()


    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.Submit -> register()
            is Intent.EmailChanged -> _state.value = _state.value.copy(email = intent.email)
            is Intent.UsernameChanged -> _state.value =
                _state.value.copy(username = intent.username)

            is Intent.PasswordChanged -> _state.value =
                _state.value.copy(password = intent.password)
        }
    }

    private fun register() {
        val current = _state.value
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val user = authRepository.signUp(
                email = current.email,
                password = current.password,
                username = current.username
            )
            if (user != null) {
                _state.update {
                    _state.value.copy(
                        isLoading = false,
                        registered = true,
                        currentUser = user
                    )
                }
            } else {
                _state.update {
                    _state.value.copy(
                        isLoading = false,
                        error = "Error en el registro"
                    )
                }
            }
        }
    }

}
