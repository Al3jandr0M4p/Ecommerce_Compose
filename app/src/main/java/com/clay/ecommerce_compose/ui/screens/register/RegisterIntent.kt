package com.clay.ecommerce_compose.ui.screens.register

sealed class Intent {
    data class EmailChanged(val email: String) : Intent()
    data class PasswordChanged(val password: String) : Intent()
    data class NameChanged(val name: String) : Intent()
    data class LastNameChanged(val lastname: String) : Intent()
    object Submit : Intent()
}
