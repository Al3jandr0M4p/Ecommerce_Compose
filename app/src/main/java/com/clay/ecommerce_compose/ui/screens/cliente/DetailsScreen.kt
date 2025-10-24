package com.clay.ecommerce_compose.ui.screens.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.data.getRestaurants

@Composable
fun DetailsScreen(id: Int?, navController: NavHostController) {
    val restaurants = getRestaurants().filter { restaurant ->
        restaurant.id == id
    }

    restaurants.forEach { restaurant ->
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = restaurant.imgUrl),
                contentDescription = restaurant.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = restaurant.title)
            restaurant.description?.let { description ->
                Text(text = description)
            }
        }

        val products = restaurant.products

        products.forEach { product ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = product.imgUrl),
                    contentDescription = product.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate(route = "product/${product.id}")
                    }
                )

                Text(text = product.title)
                restaurant.description?.let { description ->
                    Text(text = description)
                }
            }
        }
    }

}
