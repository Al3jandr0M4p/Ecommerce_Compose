package com.clay.ecommerce_compose.ui.screens.register.business

import android.net.Uri

sealed class Intent {
    data class NameChanged(val name: String) : Intent()
    data class EmailChanged(val email: String) : Intent()
    data class PasswordChanged(val password: String) : Intent()
    data class LogoChanged(val logo: Uri?) : Intent()
    data class HorarioAperturaChanged(val horario_apertura: String) : Intent()
    data class HorarioCierreChanged(val horario_cierre: String) : Intent()
    data class TelefonoChanged(val telefono: String) : Intent()
    data class HasDeliveryChanged(val hasDelivery: Boolean) : Intent()
    data class CategoryChanged(val category: String) : Intent()
    data class RncChanged(val rnc: String) : Intent()
    data class NcfChanged(val ncf: String) : Intent()
    object Submit : Intent()
}
