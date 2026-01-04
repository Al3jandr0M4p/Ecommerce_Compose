package com.clay.ecommerce_compose.ui.screens.register.business

import android.net.Uri

data class RegisterBusinessState(
    // Auth
    val email: String = "",
    val password: String = "",

    // Step 1
    val name: String = "",
    val logo: Uri? = null,
    val logoByteArray: ByteArray? = null,
    val category: String = "",
    val horario_apertura: String = "",
    val horario_cierre: String = "",
    val telefono: String = "",
    val hasDelivery: Boolean = false,
    val rnc: String = "",
    val ncf: String = "",

    // UI State
    val isLoading: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val error: String? = null,

    val businessId: Int? = null,
)
