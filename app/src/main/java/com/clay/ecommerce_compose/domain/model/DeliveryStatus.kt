package com.clay.ecommerce_compose.domain.model

enum class DeliveryStatus(val value: String) {
    PENDING("PENDING"),
    IN_TRANSIT("IN_TRANSIT"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED")
}