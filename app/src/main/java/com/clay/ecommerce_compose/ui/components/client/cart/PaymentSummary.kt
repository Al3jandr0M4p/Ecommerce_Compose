package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

@Composable
fun PaymentSummary(
    subtotal: Double,
    deliveryFee: Double,
    serviceFee: Double,
    itbis: Double,
    total: Double
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Resumen del pago",
            fontSize = 20.sp,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        SummaryRow("Precio", subtotal)
        SummaryRow("Delivery", deliveryFee)
        SummaryRow("Cargo de servicios", serviceFee)
        SummaryRow("ITBIS (18%)", itbis)

        HorizontalDivider(thickness = 2.5.dp, color = colorResource(id = R.color.lightGrey))

        SummaryRow("Total payment", total, isTotal = true)
    }
}
