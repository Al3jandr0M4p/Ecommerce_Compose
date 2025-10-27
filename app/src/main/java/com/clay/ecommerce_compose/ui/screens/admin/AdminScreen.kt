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
            .padding(50.dp),
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
            onClick = { /* navController.navigate("manageUsers") */ }
        )
        AdminCard(
            title = "Inventario",
            icon = Icons.Default.Inventory,
            onClick = { /* navController.navigate("manageInventory") */ }
        )
        AdminCard(
            title = "Órdenes",
            icon = Icons.Default.ShoppingCart,
            onClick = { /* navController.navigate("manageOrders") */ }
        )
        AdminCard(
            title = "Negocios",
            icon = Icons.Default.Store,
            onClick = { /* navController.navigate("manageStores") */ }
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
            .height(80.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(icon, contentDescription = title)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
