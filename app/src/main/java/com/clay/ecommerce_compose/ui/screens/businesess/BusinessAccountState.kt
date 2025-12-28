package com.clay.ecommerce_compose.ui.screens.businesess

data class BusinessAccountProductState(
    val id: Int? = null,
    val name: String = "",
    val businessId: String = "",
    val imgUrl: String = "",
    val description: String = "",
    val price: String = "",

    val hasStockControl: Boolean = false,
    val stock: String = "",

    val isActive: Boolean = true,

    val nameError: String? = null,
    val priceError: String? = null,
    val stockError: String? = null,
    val descriptionError: String? = null
)
