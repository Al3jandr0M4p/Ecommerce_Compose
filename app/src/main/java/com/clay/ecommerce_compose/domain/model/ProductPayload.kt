package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductPayload(
    val id: Int? = null,
    val name: String,
    val description: String,
    val price: Double,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("image_url")
    val imageUrl: String,
    val stock: Int? = null,
    @SerialName("business_id")
    val businessId: String
)
