package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
fun PriceRow(
    label: String,
    value: Double,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal)
                MaterialTheme.typography.labelMedium
            else
                MaterialTheme.typography.labelSmall,
            fontSize = if (isTotal) 18.sp else 14.sp
        )

        Text(
            text = "RD${formatPrice(value)}",
            style = if (isTotal)
                MaterialTheme.typography.labelMedium
            else
                MaterialTheme.typography.labelSmall,
            fontSize = if (isTotal) 18.sp else 14.sp
        )
    }
}
