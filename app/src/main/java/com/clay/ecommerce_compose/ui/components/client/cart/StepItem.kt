package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

@Composable
fun StepItem(number: Int, label: String, isActive: Boolean, isCompleted: Boolean) {
    val isFilled = isCompleted || isActive

    val fillColor = if (isFilled) {
        colorResource(id = R.color.black)
    } else {
        colorResource(id = R.color.lightGrey)
    }

    val borderColor = if (isFilled) {
        colorResource(id = R.color.black)
    } else {
        Color.Transparent
    }

    val textColor = if (isFilled) {
        colorResource(id = R.color.white)
    } else {
        colorResource(id = R.color.black)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size = 26.dp)
                .clip(CircleShape)
                .background(color = fillColor)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = CircleShape
                )
        ) {
            Text(
                text = number.toString(),
                color = textColor,
                fontSize = 13.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(height = 4.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = colorResource(id = R.color.black),
            style = MaterialTheme.typography.labelSmall
        )

    }
}
