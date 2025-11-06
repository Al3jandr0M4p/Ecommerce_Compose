package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: String,
    val role: String?,

    val businessId: String? = null
)
