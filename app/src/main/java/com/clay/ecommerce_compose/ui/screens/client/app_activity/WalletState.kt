package com.clay.ecommerce_compose.ui.screens.client.app_activity

data class WalletState(
    val balance: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
