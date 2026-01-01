package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.R

@Composable
fun PaymentMethods(
    selectedMethod: String? = null,
    onMethodSelected: (String) -> Unit = {}
) {
    val options = listOf("Tarjeta", "Transferencia", "Wallet", "Contra-Entrega")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedMethod),
                        onClick = { onMethodSelected(option) }
                    )
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomRadioButton(
                    selected = (option == selectedMethod),
                    onClick = { onMethodSelected(option) }
                )
                if (option == "Contra-Entrega") {
                    Text(
                        text = "$option (Efectivo)",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                } else {
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            if (option != options.last()) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = colorResource(id = R.color.lightGrey)
                )
            }
        }
    }
}

@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size = 32.dp)
            .border(
                width = 2.5.dp,
                color = if (selected) colorResource(id = R.color.black) else colorResource(id = R.color.lightGrey),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(size = 14.dp)
                    .background(
                        color = colorResource(id = R.color.black),
                        shape = CircleShape
                    )
            )
        }
    }
}
