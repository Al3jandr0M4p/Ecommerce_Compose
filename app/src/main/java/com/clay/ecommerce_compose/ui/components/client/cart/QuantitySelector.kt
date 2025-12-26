package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

@Composable
fun QuantitySelector(
    quantity: Int, stock: Int, onIncrease: () -> Unit, onDecreaseOrDelete: () -> Unit
) {
    val canIncrease = quantity < (stock.coerceAtLeast(0))

    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = colorResource(id = R.color.grey))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onDecreaseOrDelete, modifier = Modifier.size(size = 28.dp)
        ) {
            if (quantity == 1) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Remove, contentDescription = null
                )
            }
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 12.dp),
            fontSize = 16.sp
        )

        IconButton(
            onClick = onIncrease, modifier = Modifier.size(size = 28.dp), enabled = canIncrease
        ) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = null
            )
        }
    }
}
