package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage

@Composable
fun ProductsCards(
    id: Int?,
    name: String,
    price: Double,
    imageUrl: String,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .height(height = 200.dp)
            .clickable { navController.navigate("products/details/${id}") }
            .clip(shape = RoundedCornerShape(size = 16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = imageUrl, contentDescription = name, contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 130.dp)
                    .clip(shape = RoundedCornerShape(size = 16.dp))
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 4.dp)) {
            Text(text = name, fontSize = 20.sp, style = MaterialTheme.typography.labelSmall)
            Text(
                text = price.toString(),
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
