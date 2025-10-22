package com.clay.ecommerce_compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector


@Immutable
sealed class Tabs(
    val title: String,
    val icon: ImageVector
) {
    object Home : Tabs(title = "Inicio", icon = Icons.Outlined.Home)
}
