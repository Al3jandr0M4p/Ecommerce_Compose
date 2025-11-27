package com.clay.ecommerce_compose.ui.screens.businesess

import android.util.Log
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch


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
    var selectedTab by remember { mutableStateOf<Tabs>(Tabs.Home) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var openSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvents.ShowMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                        Log.e("BusinessScreen", event.message)
                    }
                }
                is UIEvents.CloseSheet -> {
                    openSheet = false
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
        },
        floatingActionButton = {
            AnimatedVisibility(visible = selectedTab is Tabs.Stock) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                    FloatingActionButton(
                        onClick = { openSheet = true },
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
                    BusinessAdministrationStock(
                        navController = navController,
                        profile = profile,
                        openSheet = openSheet,
                        sheetState = sheetState,
                        viewModel = viewModel
                    ) {
                        openSheet = false
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
