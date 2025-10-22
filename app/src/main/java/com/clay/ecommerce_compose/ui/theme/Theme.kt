package com.clay.ecommerce_compose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Ecommerce_ComposeTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        typography = Typography,
        content = content
    )
}