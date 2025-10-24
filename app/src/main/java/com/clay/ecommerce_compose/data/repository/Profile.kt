package com.clay.ecommerce_compose.data.repository

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String?,
    val username: String
)
