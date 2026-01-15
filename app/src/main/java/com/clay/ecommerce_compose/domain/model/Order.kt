package com.clay.ecommerce_compose.domain.model

import com.clay.ecommerce_compose.utils.Orders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String,
    val status: Orders,
    @SerialName("payment_method")
    val paymentMethod: String
)
