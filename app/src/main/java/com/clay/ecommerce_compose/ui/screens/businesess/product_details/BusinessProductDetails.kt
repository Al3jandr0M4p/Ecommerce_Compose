package com.clay.ecommerce_compose.ui.screens.businesess.product_details

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.ui.components.business.stockConfig.EmptyStateMessage
import com.clay.ecommerce_compose.ui.components.business.stockConfig.ProductDetails
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@Composable
fun BusinessProductDetails(
    businessId: Int?,
    productId: Int?,
    viewModel: BusinessAccountViewModel,
    navController: NavHostController
) {

    LaunchedEffect(Unit) {
        viewModel.loadProductsBusinessById(businessId)
    }

    val products by viewModel.businessProduct.collectAsState()
    val product = products?.find { it.id == productId }

    Log.e("BusinessProductDetails", "Products: $products")
    Log.e("BusinessProductDetails", "Product: $product")

    product?.let {
        LaunchedEffect(it.id) {
            viewModel.setSelectedProduct(it)
        }

        ProductDetails(
            id = it.id,
            name = it.name,
            price = it.price,
            imageUrl = it.imageUrl,
            stock = it.stock,
            description = it.description,
            categoryId = it.categoryId,
            businessId = it.businessId,
            navController = navController
        )
    } ?: run {
        EmptyStateMessage(
            message = "Error al cargar Informacion del producto",
            navController = navController
        )
    }
}
