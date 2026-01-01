package com.clay.ecommerce_compose.ui.screens.client.cart.checkout

sealed interface CheckoutIntent {
    data class PlaceOrder(
        val paymentMethod: String,
        val couponCode: String?
    ) : CheckoutIntent

    data object ClearError : CheckoutIntent
}
