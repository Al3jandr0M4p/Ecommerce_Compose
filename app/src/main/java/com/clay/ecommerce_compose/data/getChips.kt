package com.clay.ecommerce_compose.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.GeneratingTokens
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class Chips(
    val title: String,
    val icon: ImageVector
)

fun getChips(): List<Chips> {
    return listOf(
        Chips(title = "Recoger", icon = Icons.AutoMirrored.Filled.DirectionsWalk),
        Chips(title = "Ofertas", icon = Icons.Default.LocalOffer),
        Chips(title = "Rating", icon = Icons.Default.GeneratingTokens)
    )
}