package com.clay.ecommerce_compose.ui.screens.register

import com.clay.ecommerce_compose.domain.model.Profile

data class State(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val lastname: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val registered: Boolean = false,
    val currentUser: Profile? = null
)
