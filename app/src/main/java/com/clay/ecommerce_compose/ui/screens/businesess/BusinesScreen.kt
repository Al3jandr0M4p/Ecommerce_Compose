package com.clay.ecommerce_compose.ui.screens.businesess

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.navigation.Tabs
import com.clay.ecommerce_compose.ui.components.bars.MyBottomNavigationBar
import com.clay.ecommerce_compose.ui.components.bars.MyBusinessTopAppBar
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationBalance
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationConfiguration
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationHome
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationStock


@Composable
fun BusinessScreen(
    businessId: String,
    navController: NavHostController,
    viewModel: BusinessAccountViewModel,
) {
    val businessProfile = viewModel.businessProfile.collectAsState()
    val profile = businessProfile.value

    LaunchedEffect(businessId) {
        viewModel.loadBusinessById(businessId)
    }

    when (profile) {
        null -> {
            // Por ahora seria un mensaje pero despues sera un skeleton
            Text(text = "Cargando datos del negocio")
        }

        else -> {
            BusinessHomeScreen(
                navController = navController,
                viewModel = viewModel,
                profile = profile
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessHomeScreen(
    navController: NavHostController,
    viewModel: BusinessAccountViewModel,
    profile: BusinessProfile?
) {
    var selectedTab by remember { mutableStateOf<Tabs>(Tabs.Home) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MyBusinessTopAppBar(
                businessName = profile?.name ?: "",
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            MyBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { newTab ->
                    selectedTab = newTab
                },
                tabs = listOf(Tabs.Home, Tabs.Balance, Tabs.Stock, Tabs.Configuration)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            when (selectedTab) {
                is Tabs.Home -> {
                    BusinessAdministrationHome(
                        business = profile,
                        viewModel = viewModel,
                        navController = navController
                    )
                }

                is Tabs.Balance -> {
                    BusinessAdministrationBalance()
                }

                is Tabs.Stock -> {
                    BusinessAdministrationStock()
                }

                is Tabs.Configuration -> {
                    BusinessAdministrationConfiguration()
                }

                is Tabs.Activity -> {}
            }
        }
    }
}
