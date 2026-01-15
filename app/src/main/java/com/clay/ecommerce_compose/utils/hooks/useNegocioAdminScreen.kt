package com.clay.ecommerce_compose.utils.hooks

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clay.ecommerce_compose.data.AppViewModelProvider
import com.clay.ecommerce_compose.ui.screens.admin.negocios.NegocioAdminViewModel

@Composable
fun useNegocioAdminScreen(): NegocioAdminViewModel {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    return viewModel(
        factory = AppViewModelProvider(application)
    )
}
