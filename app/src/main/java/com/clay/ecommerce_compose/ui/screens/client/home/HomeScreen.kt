package com.clay.ecommerce_compose.ui.screens.client.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.business.Business
import com.clay.ecommerce_compose.ui.components.client.header.HeaderUserHome
import com.clay.ecommerce_compose.ui.components.client.search.SearchBarContainer
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel,
    businessAccountViewModel: BusinessAccountViewModel
) {
    val isLoading by homeViewModel.isLoading.collectAsState()
    val hasActiveDelivery by cartViewModel.hasActiveDelivery.collectAsState()
    val businessState by businessAccountViewModel.businessProfile.collectAsState()

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
            item {
                HeaderUserHome(
                    navController = navController,
                    cartViewModel = cartViewModel,
                    businessId = businessState?.id
                )
            }

            item { SearchBarContainer(navController = navController) }

            item { Spacer(modifier = Modifier.height(height = 20.dp)) }

            item {
                Business(
                    navController = navController,
                    viewModel = homeViewModel
                )
            }
        }

        if (hasActiveDelivery) {
            FloatingActionButton(
                shape = RoundedCornerShape(size = 10.dp),
                onClick = { navController.navigate(route = "delivery/user") },
                modifier = Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(all = 16.dp)
                    .width(width = 200.dp),
                containerColor = colorResource(id = R.color.black)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(space = 6.dp)) {
                    Icon(
                        imageVector = Icons.Default.SportsMotorsports,
                        contentDescription = null,
                        tint = colorResource(id = R.color.white)
                    )

                    Text(
                        text = "Orden",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.Companion.align(Alignment.TopCenter)
        )
    }
}
