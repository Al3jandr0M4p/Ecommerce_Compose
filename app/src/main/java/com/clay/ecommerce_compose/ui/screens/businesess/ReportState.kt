package com.clay.ecommerce_compose.ui.screens.businesess

import com.clay.ecommerce_compose.domain.model.LowStockProduct
import com.clay.ecommerce_compose.domain.model.StockMovement
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class BusinessReportsState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val totalOrders: Int = 0,
    val totalRevenue: Double = 0.0,

    val totalDiscounts: Double = 0.0,
    val lowStock: List<LowStockProduct> = emptyList(),
    val stockMovements: List<StockMovement> = emptyList(),
    val topProducts: List<TopProductReport> = emptyList(),
)

@Serializable
data class TopProductReport(
    @SerialName("product_id")
    val productId: Int,
    val name: String,
    val sold: Int,
    val revenue: Double
)
