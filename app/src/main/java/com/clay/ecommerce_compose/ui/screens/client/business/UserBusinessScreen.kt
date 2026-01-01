package com.clay.ecommerce_compose.ui.screens.client.business

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.ui.components.client.business.UserBusinessComponent
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserBusinessScreen(
    navController: NavHostController,
    idBusiness: Int?,
    cartViewModel: CartViewModel,
    viewModel: ModelViewUserBusiness,
) {
    val buss by viewModel.businessInfo.collectAsState()
    val products by viewModel.businessProductsInfo.collectAsState()

    LaunchedEffect(idBusiness) {
        viewModel.getBusinessInfoById(id = idBusiness)
        viewModel.getProductsByBusinessId(id = idBusiness)
    }
    val scrollState = rememberLazyListState()

    LaunchedEffect(products) {
        cartViewModel.checkLowStock(products)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        UserBusinessComponent(
            innerPadding = innerPadding,
            scrollState = scrollState,
            buss = buss,
            products = products,
            navController = navController,
            cartViewModel = cartViewModel
        )
    }
}
