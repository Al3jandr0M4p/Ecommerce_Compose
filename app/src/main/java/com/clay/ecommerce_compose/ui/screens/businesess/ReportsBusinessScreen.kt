package com.clay.ecommerce_compose.ui.screens.businesess

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.domain.model.LowStockProduct
import com.clay.ecommerce_compose.domain.model.StockMovement
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun ReportsBusinessScreen(viewModel: BusinessAccountViewModel, businessId: String) {
    val state by viewModel.reportsState.collectAsState()

    LaunchedEffect(businessId) {
        viewModel.loadBusinessReports(businessId)
    }

    when {
        state.isLoading -> {}
        state.error != null -> {}
        else -> {
            ReportsContent(
                revenue = state.totalRevenue,
                orders = state.totalOrders,
                lowStock = state.lowStock,
                movements = state.stockMovements,
                discounts = state.totalDiscounts,
                topProducts = state.topProducts,
            )
        }
    }
}

@Composable
fun ReportsContent(
    revenue: Double,
    orders: Int,
    lowStock: List<LowStockProduct>,
    movements: List<StockMovement>,
    discounts: Double,
    topProducts: List<TopProductReport>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            ReportsSummarySection(
                revenue = revenue,
                orders = orders,
                discounts = discounts
            )
        }

        if (lowStock.isNotEmpty()) {
            item {
                Text(
                    text = "Stock bajo",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(lowStock) { product ->
                LowStockItem(product)
            }
        }

        if (movements.isNotEmpty()) {
            item {
                Text(
                    text = "Movimientos recientes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(movements.take(10)) { movement ->
                StockMovementItem(movement)
            }
        }

        if (topProducts.isNotEmpty()) {
            item {
                Text(
                    text = "Top productos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(topProducts) { product ->
                TopProductItem(product)
            }
        }
    }
}

@Composable
private fun ReportsSummarySection(
    revenue: Double,
    orders: Int,
    discounts: Double
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                title = "Ingresos",
                value = "RD$ ${formatPrice(revenue)}",
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Ordenes",
                value = orders.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                title = "Descuentos",
                value = "RD$ ${formatPrice(discounts)}",
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Promedio",
                value = if (orders > 0)
                    "RD$ ${formatPrice(revenue / orders)}"
                else
                    "RD$ 0.00",
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun LowStockItem(product: LowStockProduct) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text(
                "Stock: ${product.currentStock} / Min: ${product.minStock}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun StockMovementItem(movement: StockMovement) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = movement.movementType.toString().uppercase(),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    "Cantidad: ${movement.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                movement.createdAt!!.substring(0, 10),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TopProductItem(product: TopProductReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${product.sold} vendidos",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "RD$ ${formatPrice(product.revenue)}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
