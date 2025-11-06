package com.clay.ecommerce_compose.ui.screens.register.business

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterBusinessViewModel(
    private val authRepository: AuthRepository,
    private val application: Application
) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(value = RegisterBusinessState())
    val state: StateFlow<RegisterBusinessState> = _state.asStateFlow()

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.NameChanged -> _state.update { it.copy(name = intent.name) }
            is Intent.EmailChanged -> _state.update { it.copy(email = intent.email) }
            is Intent.PasswordChanged -> _state.update { it.copy(password = intent.password) }
            is Intent.LogoChanged -> {
                _state.update { it.copy(logo = intent.logo) }
                viewModelScope.launch {
                    val byteArray = intent.logo?.let { uri ->
                        getApplication<Application>().contentResolver.openInputStream(uri)?.use {
                            it.readBytes()
                        }
                    }
                    _state.update { it.copy(logoByteArray = byteArray) }
                }
            }

            is Intent.DireccionChanged -> _state.update { it.copy(direccion = intent.direccion) }
            is Intent.HorarioAperturaChanged -> _state.update { it.copy(horario_apertura = intent.horarario_apertura) }
            is Intent.HorarioCierreChanged -> _state.update { it.copy(horario_cierre = intent.horario_cierre) }
            is Intent.TelefonoChanged -> _state.update { it.copy(telefono = intent.telefono) }
            is Intent.HasDeliveryChanged -> _state.update { it.copy(hasDelivery = intent.hasDelivery) }
            is Intent.CategoryChanged -> _state.update { it.copy(category = intent.category) }
            is Intent.NextStep -> {
                if (validateStep1()) {
                    _state.update { it.copy(currentPage = 2, error = null) }
                }
            }

            is Intent.PreviousStep -> _state.update { it.copy(currentPage = 1) }
            is Intent.Submit -> registerBusiness()
        }
    }

    private fun validateStep1(): Boolean {
        val currentState = _state.value
        return if (currentState.name.isBlank() || currentState.email.isBlank()) {
            _state.update { it.copy(error = "Todos los campos son obligatorios") }
            false
        } else {
            true
        }
    }

    private fun registerBusiness() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank() || currentState.direccion.isBlank()) {
            _state.update { it.copy(error = "Por favor, complete todos los campos") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val businessProfile = authRepository.signUpBusiness(
                    email = currentState.email,
                    password = currentState.password,

                    name = currentState.name,
                    logo = currentState.logo,
                    logoByteArray = currentState.logoByteArray,
                    horarioApertura = currentState.horario_apertura,
                    horarioCierre = currentState.horario_cierre,
                    category = currentState.category,

                    direccion = currentState.direccion,
                    telefono = currentState.telefono,
                    hasDelivery = currentState.hasDelivery
                )

                Log.i("BusinessViewModel", "Business $businessProfile")

                if (businessProfile != null) {
                    Log.d("BusinessViewModel", "Registro exitoso: $businessProfile")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRegistrationSuccessful = true,
                            businessId = businessProfile.id
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo completar el registro"
                        )
                    }
                    Log.e("BusinessViewModel", "Registro fallido en else $businessProfile")
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ocurrio un error al registrar el negocio"
                    )
                }
                Log.e("BusinessViewModel", "Registro fallido en catch ${e.message}")
            }
        }
    }
}
