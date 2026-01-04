package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessAdministrationStock(
    navController: NavHostController,
    viewModel: BusinessAccountViewModel,
    openSheet: Boolean,
    sheetState: SheetState,
    onCloseSheet: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val products by viewModel.businessProduct.collectAsState()
    val productsByCategory by viewModel.productsByCategory.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()
    val selectedProductForRestock by viewModel.selectedProductForRestock.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Por Categoría", "Todos", "Stock Bajo")

    val currentBusinessId = remember { state.businessId.toIntOrNull() }

    LaunchedEffect(currentBusinessId) {
        if (currentBusinessId != null) {
            viewModel.loadProductsBusinessById(currentBusinessId)
            viewModel.loadProductsByCategory(currentBusinessId)
            viewModel.loadLowStockProducts(currentBusinessId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (lowStockProducts.isNotEmpty() && selectedTab != 2) {
            StockAlertBanner(
                count = lowStockProducts.size,
                onClick = { selectedTab = 2 }
            )
        }

        // Tabs para diferentes vistas
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) colorResource(id = R.color.black) else colorResource(
                                id = R.color.coolGrey
                            )
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> ProductsByCategoryView(
                productsByCategory = productsByCategory,
                navController = navController,
            )

            1 -> AllProductsView(
                products = products ?: emptyList(),
                navController = navController,
            )

            2 -> LowStockView(
                lowStockProducts = lowStockProducts,
                navController = navController,
                onRestockClick = { productId ->
                    viewModel.openRestockDialog(productId)
                }
            )
        }

        if (openSheet) {
            Stepper(
                sheetState = sheetState,
                onDimiss = { onCloseSheet() },
                viewModel = viewModel,
            )
        }

        selectedProductForRestock?.let { productId ->
            val product = products?.find { it.id == productId }
            product?.let {
                RestockDialog(
                    productId = productId,
                    productName = it.name,
                    currentStock = it.stock,
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeRestockDialog() }
                )
            }
        }
    }
}
