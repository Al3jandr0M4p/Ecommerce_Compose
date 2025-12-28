package com.clay.ecommerce_compose.ui.screens.client.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.navigation.Tabs
import com.clay.ecommerce_compose.ui.components.bars.MyBottomNavigationBar
import com.clay.ecommerce_compose.ui.components.client.header.HeaderUserHome
import com.clay.ecommerce_compose.ui.screens.client.app_activity.Activity
import com.clay.ecommerce_compose.ui.screens.client.app_activity.TransactionsViewModel
import com.clay.ecommerce_compose.ui.screens.client.app_activity.WalletViewModel
import com.clay.ecommerce_compose.ui.screens.client.cart.CartIntent
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.ui.screens.client.config.ConfigViewModel
import com.clay.ecommerce_compose.ui.screens.client.config.Configuration

@Composable
fun UserHomeScreen(
    navController: NavHostController,
    configViewModel: ConfigViewModel,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel,
    walletViewModel: WalletViewModel,
    transactionsViewModel: TransactionsViewModel
) {
    LaunchedEffect(Unit) {
        homeViewModel.loadBusiness()
        cartViewModel.handleIntent(intent = CartIntent.LoadCart)
    }

    var selectedTab by remember { mutableStateOf<Tabs>(value = Tabs.Home) }
    val pages: Map<Tabs, @Composable () -> Unit> = mapOf(
        Tabs.Home to {
            Home(
                navController = navController,
                cartViewModel = cartViewModel,
                homeViewModel = homeViewModel
            )
        },
        Tabs.Activity to {
            Activity(
                walletViewModel = walletViewModel,
                transactionsViewModel = transactionsViewModel
            )
        },

        Tabs.Configuration to {
            Configuration(
                navController = navController,
                configViewModel = configViewModel,
            )
        }
    )

    Scaffold(
        bottomBar = {
            MyBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { newTab ->
                    selectedTab = newTab
                },
                tabs = listOf(Tabs.Home, Tabs.Activity, Tabs.Configuration)
            )
        },
        containerColor = colorResource(id = R.color.white),
        modifier = Modifier.fillMaxWidth(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            pages[selectedTab]?.invoke()
        }
    }
}
