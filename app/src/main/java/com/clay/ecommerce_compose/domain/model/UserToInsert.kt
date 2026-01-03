package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserToInsert(
    val email: String,
    val password: String,
    @SerialName("username")
    val userName: String,
    @SerialName("role_name")
    val roleName: String
)
