package com.clay.ecommerce_compose.domain.model

data class User(
    val id: String?,
    val name: String,
    val email: String,
    val role: String,
    val status: String
)
