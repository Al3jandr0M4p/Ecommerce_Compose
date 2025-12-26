package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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

    LaunchedEffect(state.id) {
        viewModel.loadProductsBusinessById(businessId = state.id)
    }

    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (products != null) {
            for (product in products!!) {
                ProductsCards(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    navController = navController
                )
            }
        } else {
            Text(
                text = "No hay productos creados",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W600),
                fontSize = 16.sp
            )
        }

        if (openSheet) {
            Stepper(
                sheetState = sheetState,
                onDimiss = { onCloseSheet() },
                viewModel = viewModel,
            )
        }
    }
}
