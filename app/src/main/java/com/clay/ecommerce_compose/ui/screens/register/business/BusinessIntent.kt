package com.clay.ecommerce_compose.ui.screens.register.business

import android.net.Uri

sealed class Intent {
    data class NameChanged(val name: String) : Intent()
    data class EmailChanged(val email: String) : Intent()
    data class PasswordChanged(val password: String) : Intent()
    data class LogoChanged(val logo: Uri?) : Intent()
    data class DireccionChanged(val direccion: String) : Intent()
    data class HorarioAperturaChanged(val horarario_apertura: String) : Intent()
    data class HorarioCierreChanged(val horario_cierre: String) : Intent()
    data class TelefonoChanged(val telefono: String) : Intent()
    data class HasDeliveryChanged(val hasDelivery: Boolean) : Intent()
    data class CategoryChanged(val category: String) : Intent()
    object NextStep : Intent()
    object PreviousStep : Intent()
    object Submit : Intent()
}
