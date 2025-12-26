package com.clay.ecommerce_compose.ui.components.client.cart

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest

@Composable
fun EmptyScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(data = "https://i5.walmartimages.com/dfw/63fd9f59-e0d6/65ab57af-59d6-423a-9500-1fa5ab36d1c7/v1/empty-cart.svg?odnHeight=240&odnWidth=200&odnBg=ffffff")
                .build(),
            onError = {
                Log.e(
                    "CartError", "Error al cargar la imagen"
                )
            },
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Text(
            text = "Tu carrito esta vacio",
            fontSize = 26.sp,
            style = MaterialTheme.typography.labelSmall
        )
        Button(
            onClick = { navController.navigate(route = "userHome") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(color = 0x0000ff99), contentColor = Color.White
            ),
        ) {
            Text(
                text = "Ir a comprar",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.5.dp)
            )
        }
    }
}
