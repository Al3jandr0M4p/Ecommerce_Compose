package com.clay.ecommerce_compose.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.clay.ecommerce_compose.ui.components.AdminTopBar
import com.clay.ecommerce_compose.ui.components.AdminBottomBar

@Composable
fun UsersScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            AdminTopBar(
                title = "Gestión de Usuarios",
                navController = navController
            )
        },
        bottomBar = {
            AdminBottomBar(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.background // 👈 fondo general
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // 👈 asegura fondo del tema
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aquí se mostrarán los usuarios registrados",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
