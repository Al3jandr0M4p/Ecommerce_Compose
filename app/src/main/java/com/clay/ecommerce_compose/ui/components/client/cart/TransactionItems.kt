package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.data.repository.WalletTransaction
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun TransactionItem(transaction: WalletTransaction, onViewInvoice: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    val dateTime = transaction.createdAt.substringBefore("T")
        .split("-")
        .reversed()
        .joinToString("/")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isExpanded = !isExpanded
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    if (transaction.orderId != null) {
                        Button(
                            onClick = { onViewInvoice(transaction.orderId) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Ver factura",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        Text(
                            text = "Sin factura disponible",
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.coolGreyLight),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}
