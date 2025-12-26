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
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun SummaryRow(
    label: String,
    amount: Double,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal)
                MaterialTheme.typography.labelMedium
            else
                MaterialTheme.typography.labelSmall
        )

        Text(
            text = formatPrice(amount),
            style = if (isTotal)
                MaterialTheme.typography.labelMedium
            else
                MaterialTheme.typography.labelSmall
        )
    }
}
