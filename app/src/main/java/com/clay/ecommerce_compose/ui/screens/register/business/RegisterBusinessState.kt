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

    // Step 2
    val latitude: Double? = null,
    val longitude: Double? = null,
    val telefono: String = "",
    val hasDelivery: Boolean = false,

    // UI State
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val error: String? = null,

    val businessId: String? = null
)
