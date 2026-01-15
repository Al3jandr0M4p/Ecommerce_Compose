package com.clay.ecommerce_compose.ui.screens.register.delivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clay.ecommerce_compose.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterDeliveryViewModel(private val authRepository: AuthRepository) : ViewModel() {

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

            is Intent.Submit -> registerDelivery()
        }
    }

    private fun registerDelivery() {
        val current = _state.value
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val user = authRepository.signUpDelivery(
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
