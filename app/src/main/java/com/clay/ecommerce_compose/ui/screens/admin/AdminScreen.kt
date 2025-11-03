package com.clay.ecommerce_compose.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AdminScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(35.dp),
    ) {
        Text(
            text = "Panel de Administrador",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        AdminDashboardButtons(navController)
    }
}

@Composable
fun AdminDashboardButtons(navController: NavController) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AdminCard(
            title = "Usuarios",
            icon = Icons.Default.People,
            onClick = { navController.navigate("users") }
        )
        AdminCard(
            title = "Inventario",
            icon = Icons.Default.Inventory,
            onClick = { navController.navigate("inventory") }
        )
        AdminCard(
            title = "Órdenes",
            icon = Icons.Default.ShoppingCart,
            onClick = { navController.navigate("orders") }
        )
        AdminCard(
            title = "Negocios",
            icon = Icons.Default.Store,
            onClick = { navController.navigate("stores") }
        )
    }
}


@Composable
fun AdminCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

