package com.clay.ecommerce_compose.ui.screens.register.business

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BusinessViewModel(
    private val authRepository: AuthRepository,
    private val application: Application
) : AndroidViewModel(application) {
    data class BusinessState(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val logo: Uri? = null,
        val logoByteArray: ByteArray? = null,
        val direccion: String = "",
        val horario_apertura: String = "",
        val horario_cierre: String = "",
        val telefono: String = "",
        val description: String = "",
        val hasDelivery: Boolean = false,
        val category: String = "",
        val isLoading: Boolean = false,
        val isRegistrationSuccessful: Boolean = false,
        val error: String? = null
    )

    sealed class Intent {
        data class NameChanged(val name: String) : Intent()
        data class EmailChanged(val email: String) : Intent()
        data class PasswordChanged(val password: String) : Intent()
        data class LogoChanged(val logo: Uri?) : Intent()
        data class DireccionChanged(val direccion: String) : Intent()
        data class HorarioAperturaChanged(val horarario_apertura: String) : Intent()
        data class HorarioCierreChanged(val horario_cierre: String) : Intent()
        data class TelefonoChanged(val telefono: String) : Intent()
        data class DescriptionChanged(val description: String) : Intent()
        data class HasDeliveryChanged(val hasDelivery: Boolean) : Intent()
        data class CategoryChanged(val category: String) : Intent()
        object Submit : Intent()
    }

    private val _state = MutableStateFlow(value = BusinessState())
    val state: StateFlow<BusinessState> = _state.asStateFlow()

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
            is Intent.DescriptionChanged -> _state.update { it.copy(description = intent.description) }
            is Intent.HasDeliveryChanged -> _state.update { it.copy(hasDelivery = intent.hasDelivery) }
            is Intent.CategoryChanged -> _state.update { it.copy(category = intent.category) }
            is Intent.Submit -> registerBusiness()
        }
    }

    private fun registerBusiness() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank() || currentState.name.isBlank()) {
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
                    direccion = currentState.direccion,
                    horarioApertura = currentState.horario_apertura,
                    horarioCierre = currentState.horario_cierre,
                    telefono = currentState.telefono,
                    description = currentState.description,
                    hasDelivery = currentState.hasDelivery,
                    category = currentState.category
                )

                if (businessProfile != null) {
                    _state.update { it.copy(isLoading = false, isRegistrationSuccessful = true) }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo completar el registro"
                        )
                    }
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ocurrio un error al registrar el negocio"
                    )
                }
            }
        }
    }
}
