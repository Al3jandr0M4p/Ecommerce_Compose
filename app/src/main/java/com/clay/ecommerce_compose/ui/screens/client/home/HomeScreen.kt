package com.clay.ecommerce_compose.ui.screens.client.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.Brand
import com.clay.ecommerce_compose.navigation.Tabs
import com.clay.ecommerce_compose.ui.components.bars.MyBottomNavigationBar
import com.clay.ecommerce_compose.ui.components.client.business.Business
import com.clay.ecommerce_compose.ui.components.client.header.HeaderUserHome
import com.clay.ecommerce_compose.ui.components.client.search.SearchBarContainer
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.ui.screens.client.config.ConfigViewModel
import com.clay.ecommerce_compose.ui.screens.client.config.Configuration
import kotlinx.coroutines.delay

@Composable
fun Home(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(true) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    val business = listOf(
        Brand(
            title = "Nike",
            color = R.color.black,
            description = "Los mejores productos para ti",
            logo = R.drawable.ic_nike_logo
        ),
        Brand(
            title = "Puma",
            color = R.color.black,
            description = "No pierdas la mejor ropa deportiva",
            logo = R.drawable.ic_nike_logo
        ),
        Brand(
            title = stringResource(id = R.string.app_name),
            color = R.color.black,
            description = "Obten ${stringResource(id = R.string.app_name)} One por 15 dias",
            logo = R.drawable.ic_nike_logo
        )
    )

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
                viewModel = homeViewModel,
                brand = business,
                pagerState = pagerState
            )
        }
    }
}


@Composable
fun UserHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    configViewModel: ConfigViewModel,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel
) {
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
            Text(text = "Tu actividad")
        },

        Tabs.Configuration to {
            Configuration(
                navController = navController,
                configViewModel = configViewModel
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
        modifier = Modifier.fillMaxWidth()
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
