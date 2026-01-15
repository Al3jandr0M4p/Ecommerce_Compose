package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.Serializable

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val status: String
)

@Serializable
data class UserView(
    val id: String,
    val email: String,
    val name: String,
    val role: String
)