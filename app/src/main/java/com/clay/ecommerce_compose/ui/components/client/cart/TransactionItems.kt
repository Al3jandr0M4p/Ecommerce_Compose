package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.data.repository.WalletTransaction
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun TransactionItem(transaction: WalletTransaction) {
    val dateTime = transaction.createdAt.substringBefore("T")
        .split("-")
        .reversed()
        .joinToString("/")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Egreso",
                    tint = colorResource(id = R.color.tintRed),
                    modifier = Modifier.size(size = 24.dp)
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
                text = "-${formatPrice(amount = transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.tintRed)
            )
        }
    }
}