package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductInsertPayload(
    @SerialName("business_id")
    val businessId: Int,
    val name: String,
    val description: String? = null,
    val price: Double,
    val stock: Int,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("is_active")
    val isActive: Boolean = true
)

@Serializable
data class ProductPayload(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("image_url")
    val imageUrl: String,
    val stock: String,
    @SerialName("business_id")
    val businessId: String
)
