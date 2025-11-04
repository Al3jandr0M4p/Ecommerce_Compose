package com.clay.ecommerce_compose.ui.screens.login

import com.clay.ecommerce_compose.domain.model.Profile


data class State(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false,
    val currentUser: Profile? = null
)
