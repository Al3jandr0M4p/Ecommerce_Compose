package com.clay.ecommerce_compose.ui.screens.client.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.business.Business
import com.clay.ecommerce_compose.ui.components.client.header.HeaderUserHome
import com.clay.ecommerce_compose.ui.components.client.search.SearchBarContainer
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel
) {
    val isLoading by homeViewModel.isLoading.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { homeViewModel.loadBusiness(force = true) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white))
        ) {
            item { HeaderUserHome(navController = navController, cartViewModel = cartViewModel) }

            item { SearchBarContainer(navController = navController) }

            item { Spacer(modifier = Modifier.height(height = 20.dp)) }

            item {
                Business(
                    navController = navController,
                    viewModel = homeViewModel
                )
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.Companion.align(Alignment.TopCenter)
        )
    }
}
