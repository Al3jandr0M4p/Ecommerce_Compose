package com.clay.ecommerce_compose.utils

import kotlinx.serialization.Serializable

@Serializable
enum class Orders {
    pending,
    paid,
    cancelled,
    delivered
}
