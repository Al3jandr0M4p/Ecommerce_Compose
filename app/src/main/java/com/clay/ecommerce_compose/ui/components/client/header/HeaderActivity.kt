package com.clay.ecommerce_compose.ui.components.client.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun HeaderActivity(balance: Double, isLoading: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Tu Cartera", fontSize = 40.sp, style = MaterialTheme.typography.labelSmall)

        Text(
            text = if (isLoading) "$0" else formatPrice(amount = balance),
            fontSize = 28.sp,
            style = MaterialTheme.typography.labelSmall
        )

        // Botton para recargar
    }
}
