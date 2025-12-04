package com.clay.ecommerce_compose.ui.screens.businesess

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.navigation.Tabs
import com.clay.ecommerce_compose.ui.components.bars.MyBottomNavigationBar
import com.clay.ecommerce_compose.ui.components.bars.MyBusinessTopAppBar
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationBalance
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationConfiguration
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationHome
import com.clay.ecommerce_compose.ui.components.business.BusinessAdministrationStock
import com.clay.ecommerce_compose.utils.hooks.useBusinessScreen


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

        viewModel.handleIntent(intent = BusinessAccountProductIntent.SetBusinessId(businessId))
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
    val businessController = useBusinessScreen(viewModel = viewModel)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = businessController.snackBarHost) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MyBusinessTopAppBar(
                businessName = profile?.name ?: "",
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            MyBottomNavigationBar(
                selectedTab = businessController.selectedTab.value,
                onTabSelected = { businessController.selectedTab.value = it },
                tabs = listOf(Tabs.Home, Tabs.Balance, Tabs.Stock, Tabs.Configuration)
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = businessController.selectedTab.value is Tabs.Stock) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                    FloatingActionButton(
                        onClick = { businessController.openSheet.value },
                        modifier = Modifier.width(width = 200.dp),
                        shape = CircleShape,
                        containerColor = colorResource(id = R.color.black),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(size = 34.dp)
                                    .clip(shape = CircleShape)
                            )
                            Text(
                                text = "Agregar producto",
                                color = colorResource(id = R.color.white),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            when (businessController.selectedTab.value) {
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
                    BusinessAdministrationStock(
                        navController = navController,
                        profile = profile,
                        openSheet = businessController.openSheet.value,
                        sheetState = businessController.sheetState,
                        viewModel = viewModel
                    ) {
                        businessController.openSheet.value = false
                    }
                }

                is Tabs.Configuration -> {
                    BusinessAdministrationConfiguration()
                }

                is Tabs.Activity -> {}
            }
        }
    }
}
