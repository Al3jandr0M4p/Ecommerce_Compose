package com.clay.ecommerce_compose.ui.components.client.business

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R

@Composable
fun ProductCard(
    title: String,
    price: Double,
    image: String,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 10.dp))
            .size(width = 290.dp, height = 200.dp)
            .padding(horizontal = 10.dp)
            .clickable {},
        elevation = CardDefaults.cardElevation(defaultElevation = 0.6.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 140.dp)
                .clip(shape = RoundedCornerShape(size = 10.dp))
        )

        Spacer(modifier = Modifier.height(height = 10.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "Precio $price",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(size = 30.dp)
                        .clip(shape = RoundedCornerShape(size = 10.dp)),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = colorResource(id = R.color.black),
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = colorResource(id = R.color.white),
                        modifier = Modifier.size(size = 30.dp)
                    )
                }
            }
        }
    }
}
