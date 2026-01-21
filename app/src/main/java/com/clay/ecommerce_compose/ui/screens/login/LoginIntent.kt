package com.clay.ecommerce_compose.ui.screens.login

sealed class Intent {
    data class EmailChanged(val email: String) : Intent()
    data class PasswordChanged(val password: String) : Intent()
    data object Submit : Intent()
}
