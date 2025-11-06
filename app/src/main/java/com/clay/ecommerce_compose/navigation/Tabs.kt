package com.clay.ecommerce_compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
sealed class Tabs(
    val title: String,
    val icon: ImageVector
) {
    object Home : Tabs(title = "Inicio", icon = Icons.Filled.Home)
    object Activity : Tabs(title = "Actividad", icon = Icons.Filled.Apps)
    object Configuration : Tabs(title = "Cuenta", icon = Icons.Filled.Person)
}
