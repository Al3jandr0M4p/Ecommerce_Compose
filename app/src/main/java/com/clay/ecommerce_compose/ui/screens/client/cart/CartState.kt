package com.clay.ecommerce_compose.ui.screens.client.cart

data class CartState(
    val items: List<CartItem> = emptyList(),

    val totalPrice: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val serviceFee: Double = 0.0,
    val subTotal: Double = 0.0,

    val isLoading: Boolean = false,
    val error: String? = null
)

data class CartItem(
    val id: Int?,
    val businessName: String,
    val businessImg: String,
    val businessId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val quantity: Int,
    val stock: Int
)
