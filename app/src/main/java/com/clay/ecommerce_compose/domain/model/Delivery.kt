
package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Delivery(
    val id: String,
    val order_id: String,
    val customer_name: String,
    val delivery_person: String,
    val status: String,
    val address: String,
    val phone: String,
    val total: Double,
    val created_at: String
)