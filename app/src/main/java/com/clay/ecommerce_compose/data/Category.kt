package com.clay.ecommerce_compose.data

import androidx.compose.runtime.Immutable

@Immutable
data class Category(
    val title: String,
    val img: Int
)
