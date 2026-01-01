package com.clay.ecommerce_compose.domain.model

import com.clay.ecommerce_compose.utils.Orders

data class Order(
    val id: String,
    val status: Orders,
    val paymentMethod: String
)
