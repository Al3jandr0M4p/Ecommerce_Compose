package com.clay.ecommerce_compose.ui.screens.client.cart.checkout

import com.clay.ecommerce_compose.data.repository.CreateOrderResponse

data class CheckoutState(
    val isLoading: Boolean = false,
    val orderResult: CreateOrderResponse? = null,
    val error: String? = null
)
