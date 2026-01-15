package com.clay.ecommerce_compose.ui.screens.client.home

import android.app.Activity
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.business.Business
import com.clay.ecommerce_compose.ui.components.client.header.HeaderUserHome
import com.clay.ecommerce_compose.ui.components.client.search.SearchBarContainer
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Home(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel,
    favoritesViewModel: FavoriteViewModel
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val windowSize = calculateWindowSizeClass(activity)
    val isTablet = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

    val isLoading by homeViewModel.isLoading.collectAsState()
    val hasActiveDelivery by cartViewModel.hasActiveDelivery.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { homeViewModel.loadBusiness(force = true) }
    )

    LaunchedEffect(Unit) {
        while (true) {
            cartViewModel.refreshActiveOrder()
            delay(10000L)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cartViewModel.refreshActiveOrder()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                )
            }

            item { SearchBarContainer(navController = navController) }

            item { Spacer(modifier = Modifier.height(height = if (isTablet) 30.dp else 20.dp)) }

            item {
                Business(
                    navController = navController,
                    viewModel = homeViewModel,
                    isTablet = isTablet,
                    favoritesViewModel = favoritesViewModel
                )
            }
        }

        if (hasActiveDelivery) {
            FloatingActionButton(
                shape = RoundedCornerShape(size = if (isTablet) 18.dp else 10.dp),
                onClick = { navController.navigate(route = "delivery/user") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(all = if (isTablet) 20.dp else 16.dp)
                    .width(width = if (isTablet) 400.dp else 200.dp),
                containerColor = colorResource(id = R.color.black)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = if (isTablet) 8.dp else 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsMotorsports,
                        contentDescription = null,
                        tint = colorResource(id = R.color.white)
                    )

                    Text(
                        text = "Orden",
                        fontSize = if (isTablet) 22.sp else 16.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
