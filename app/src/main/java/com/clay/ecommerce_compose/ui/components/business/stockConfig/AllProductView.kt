package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.domain.model.ProductPayload

@Composable
fun AllProductsView(
    products: List<ProductPayload>,
    navController: NavHostController,
) {
    if (products.isEmpty()) {
        EmptyStateMessage("No hay productos creados")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                val isLowStock = product.stock <= product.minStock

                ProductsCards(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    imageUrl = product.imageUrl ?: "",
                    stock = product.stock,
                    minStock = product.minStock,
                    isLowStock = isLowStock,
                    navController = navController
                )
            }
        }
    }
}