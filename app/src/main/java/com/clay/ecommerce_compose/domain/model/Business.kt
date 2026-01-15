package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BusinessProfile(
    val id: Int,
    @SerialName("owner_id")
    val ownerId: String = "",
    val name: String = "",
    @SerialName("logo_url")
    val logoUrl: String? = null,
    @SerialName("longitude")
    val longitud: Double? = null,
    @SerialName("latitude")
    val latitud: Double? = null,
    @SerialName("opening_time")
    val horarioApertura: String = "",
    @SerialName("closing_time")
    val horarioCierre: String = "",
    val phone: String = "",
    @SerialName("has_delivery")
    val hasDelivery: Boolean = false,
    val category: String = "",
    val rnc: String = "",
    val ncf: String = ""
)

@Serializable
data class Business(
    val id: Int? = null,
    val owner_id: String,
    val name: String,
    val rnc: String,
    val ncf: String,
    val opening_time: String,
    val closing_time: String,
    val phone: String,
    val description: String,
    val has_delivery: Boolean,
    val category: String,
    val logo_url: String,
    val status: String,
    val created_at: String? = null
)

