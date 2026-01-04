package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemEntity(
    val id: Int,
    @SerialName("user_id")
    val userId: String,
    @SerialName("product_id")
    val productId: Int,
    @SerialName("business_id")
    val businessId: Int,
    val quantity: Int,
    val name: String,
    val price: Double,
    val stock: Int,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("business_name")
    val businessName: String,
    @SerialName("business_img")
    val businessImg: String
)