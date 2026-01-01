package com.clay.ecommerce_compose.ui.screens.client.cart.coupon

data class Coupon(
    val code: String,
    val type: CouponType,
    val value: Double,
    val minPurchase: Double = 0.0,
    val maxDiscount: Double? = null,
    val isActive: Boolean = true
)

enum class CouponType {
    PERCENTAGE, // Descuento en porcentaje
    FIXED // Descuento en monto fijo
}

sealed class CouponResult {
    data class Success(val coupon: Coupon, val discount: Double) : CouponResult()
    data class Error(val message: String) : CouponResult()
}
