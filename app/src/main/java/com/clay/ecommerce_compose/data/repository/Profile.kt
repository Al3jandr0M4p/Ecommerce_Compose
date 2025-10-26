package com.clay.ecommerce_compose.data.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    @SerialName("role_id")
    val roleId: Int? = null,
    var roleName: String? = null
)
