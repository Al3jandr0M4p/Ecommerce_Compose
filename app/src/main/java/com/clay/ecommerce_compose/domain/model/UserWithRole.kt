package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserWithRole(
    val id: String,
    val role_id: Int,
    val email: String? = null,
    val created_at: String? = null
)

@Serializable
data class UserWithEmail(
    val id: String,
    val email: String? = null
)