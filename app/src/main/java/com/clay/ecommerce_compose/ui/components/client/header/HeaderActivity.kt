package com.clay.ecommerce_compose.ui.components.client.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun HeaderActivity(balance: Double, isLoading: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Cartera",
            fontSize = 40.sp,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W500)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ) {
            Column(modifier = Modifier.padding(all = 20.dp)) {
                Text(
                    text = "Balance",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W400),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(height = 12.dp))

                Text(
                    text = if (isLoading) "$0" else formatPrice(amount = balance),
                    fontSize = 44.sp,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W500)
                )

                Spacer(modifier = Modifier.height(height = 20.dp))

                // Botton para recargar
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFFF5F5F5),
                        contentColor = colorResource(id = R.color.black)
                    ),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(size = 50.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Recargar",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
