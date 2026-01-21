package com.clay.ecommerce_compose.ui.screens.register.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.auth.business.StepContent
import com.clay.ecommerce_compose.ui.components.auth.register_business_header.BusinessHeaderRegister

@Composable
fun RegisterBusiness(viewModel: RegisterBusinessViewModel, navController: NavHostController) {
    var showPassword by remember { mutableStateOf(value = true) }
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.isRegistrationSuccessful) {
        if (state.isRegistrationSuccessful) {
            navController.navigate(route = "businessHome/${state.businessId}") {
                popUpTo(route = "registerBusiness") {
                    inclusive = true
                }
            }
        }
    }
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.white))
    ) {
        BusinessHeaderRegister(navController = navController)

        StepContent(
            state = state,
            onIntent = viewModel::handleIntent,
            showPassword = showPassword,
            onShowPassword = { showPassword = !showPassword },
        )
    }
}
