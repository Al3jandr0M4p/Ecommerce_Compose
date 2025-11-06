package com.clay.ecommerce_compose.ui.screens.businesess

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.ui.components.business.BusinessInfoContent

@Composable
fun BusinessScreen(
    businessId: String,
    navController: NavHostController,
    viewModel: BusinessAccountViewModel
) {
    val businessProfile = viewModel.businessProfile.collectAsState()

    LaunchedEffect(businessId) {
        viewModel.loadBusinessById(businessId)
    }

    when (val profile = businessProfile.value) {
        null -> {
            // Por ahora seria un mensaje pero despues sera un skeleton
            Text(text = "Cargando datos del negocio")
        }

        else -> {
            BusinessInfoContent(
                business = profile,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
