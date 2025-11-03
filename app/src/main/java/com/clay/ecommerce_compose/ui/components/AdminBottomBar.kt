package com.clay.ecommerce_compose.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class AdminBottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Users : AdminBottomNavItem("users", "Usuarios", Icons.Default.People)
    object Inventory : AdminBottomNavItem("inventory", "Inventario", Icons.Default.Inventory)
    object Orders : AdminBottomNavItem("orders", "Órdenes", Icons.Default.ShoppingCart)
    object Stores : AdminBottomNavItem("stores", "Negocios", Icons.Default.Store)
}

@Composable
fun AdminBottomBar(navController: NavController, modifier: Modifier = Modifier) {
    val items = listOf(
        AdminBottomNavItem.Users,
        AdminBottomNavItem.Inventory,
        AdminBottomNavItem.Orders,
        AdminBottomNavItem.Stores
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier.height(65.dp),
        tonalElevation = 6.dp,
        containerColor = MaterialTheme.colorScheme.primary // 💜 fondo morado
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                },
                label = {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        } // ← cierre del forEach
    } // ← cierre del NavigationBar
} // ← cierre de la función AdminBottomBar
