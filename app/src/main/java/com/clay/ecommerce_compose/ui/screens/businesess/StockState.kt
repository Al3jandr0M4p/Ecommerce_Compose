package com.clay.ecommerce_compose.ui.screens.businesess

import com.clay.ecommerce_compose.domain.model.LowStockProduct
import com.clay.ecommerce_compose.domain.model.ProductCategory
import com.clay.ecommerce_compose.domain.model.ProductsByCategory

data class StockManagementState(
    val categories: List<ProductCategory> = emptyList(),
    val productsByCategory: List<ProductsByCategory> = emptyList(),
    val lowStockProducts: List<LowStockProduct> = emptyList(),
    val selectedCategory: ProductCategory? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showLowStockAlert: Boolean = false
)
