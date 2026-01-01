package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.domain.model.ProductsByCategory

@Composable
fun ProductsByCategoryView(
    productsByCategory: List<ProductsByCategory>,
    navController: NavHostController,
) {
    if (productsByCategory.isEmpty()) {
        EmptyStateMessage(message = "No hay productos organizados por categoría", navController = navController)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productsByCategory) { categoryGroup ->
                CategorySection(
                    categoryName = categoryGroup.categoryName,
                    productCount = categoryGroup.productCount,
                    products = categoryGroup.products,
                    navController = navController
                )
            }
        }
    }
}