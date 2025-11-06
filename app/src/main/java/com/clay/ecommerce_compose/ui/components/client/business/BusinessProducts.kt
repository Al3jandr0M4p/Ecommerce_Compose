package com.clay.ecommerce_compose.ui.components.client.business

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ProductCard(
    title: String,
    price: String,
    image: Int,
    onAddClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(160.dp) // Ajusta para que no ocupe toda la pantalla
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Precio: $price",
                fontSize = 12.sp,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }

        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
                .size(26.dp)
                .background(Color.Black, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
