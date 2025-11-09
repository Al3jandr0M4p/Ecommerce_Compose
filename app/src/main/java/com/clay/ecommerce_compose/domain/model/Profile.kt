package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String? = null,
    @SerialName("role_id")
    val roleId: Int? = null,
    var roleName: String? = null,

    val businessId: String? = null,
)
