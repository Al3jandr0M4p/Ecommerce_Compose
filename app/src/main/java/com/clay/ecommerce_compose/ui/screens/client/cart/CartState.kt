package com.clay.ecommerce_compose.ui.screens.client.cart

import com.clay.ecommerce_compose.domain.model.Order
import com.clay.ecommerce_compose.ui.screens.client.cart.coupon.Coupon

data class CartState(
    val items: List<CartItem> = emptyList(),

    val totalPrice: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val serviceFee: Double = 0.0,
    val subTotal: Double = 0.0,

    val isLoading: Boolean = false,
    val error: String? = null,

    val appliedCoupon: Coupon? = null,
    val couponDiscount: Double = 0.0,
    val couponError: String? = null,

    val selectedPaymentMethod: String? = null,
    val activeOrder: Order? = null,
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
