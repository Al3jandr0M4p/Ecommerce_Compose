package com.clay.ecommerce_compose.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.domain.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


// =========== MODEL =========== //
class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // =========== STATE =========== //
    data class State(
        val email: String = "",
        val password: String = "",
        val name: String = "",
        val lastname: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val registered: Boolean = false,
        val currentUser: Profile? = null
    )

    // =========== INTENT =========== //
    sealed class Intent {
        data class EmailChanged(val email: String) : Intent()
        data class PasswordChanged(val password: String) : Intent()
        data class NameChanged(val name: String) : Intent()
        data class LastNameChanged(val lastname: String) : Intent()
        object Submit : Intent()
    }

    private val _state = MutableStateFlow(value = State())
    val state: StateFlow<State> = _state.asStateFlow()


    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.EmailChanged -> _state.value =
                _state.value.copy(email = intent.email)

            is Intent.NameChanged -> _state.value =
                _state.value.copy(name = intent.name)

            is Intent.LastNameChanged -> _state.value =
                _state.value.copy(lastname = intent.lastname)

            is Intent.PasswordChanged -> _state.value =
                _state.value.copy(password = intent.password)

            is Intent.Submit -> register()
        }
    }

    private fun register() {
        val current = _state.value
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val user = authRepository.signUp(
                    email = current.email,
                    password = current.password,
                    name = current.name,
                    lastname = current.lastname
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
            } catch (e: Exception) {
                _state.update {
                    _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }

            }
        }
    }

}
