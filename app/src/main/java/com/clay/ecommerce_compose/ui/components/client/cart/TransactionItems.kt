package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.data.repository.WalletTransaction
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun TransactionItem(transaction: WalletTransaction) {
    val isCredit = transaction.type == "credit"
    val icon = if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward
    val color = if (isCredit) Color(0xFF4CAF50) else Color(0xFFF44336)

    val dateTime = transaction.createdAt.substringBefore("T")
        .split("-")
        .reversed()
        .joinToString("/")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (isCredit) "Ingreso" else "Egreso",
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )

                Column {
                    Text(
                        text = transaction.reason ?: "Sin descripción",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = dateTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${if (isCredit) "+" else "-"}${formatPrice(transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = color
            )
        }
    }
}