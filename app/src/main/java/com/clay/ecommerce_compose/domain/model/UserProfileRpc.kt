package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileRpc(
    val id: String,
    val name: String?,
    val email: String?,
    @SerialName("role_name")
    val roleName: String?
)