package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BusinessProfile(
    val id: String = "",

    @SerialName("owner_id")
    val ownerId: String = "",

    val name: String = "",

    @SerialName("logo_url")
    val logoUrl: String? = null,

    @SerialName("address")
    val direccion: String = "",

    @SerialName("opening_time")
    val horarioApertura: String = "",

    @SerialName("closing_time")
    val horarioCierre: String = "",

    val phone: String = "",
    val description: String = "",

    @SerialName("has_delivery")
    val hasDelivery: Boolean = false,

    val category: String = ""
)
