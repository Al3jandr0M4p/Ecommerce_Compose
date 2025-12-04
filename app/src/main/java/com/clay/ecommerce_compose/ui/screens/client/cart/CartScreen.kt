package com.clay.ecommerce_compose.ui.screens.client.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.utils.hooks.useCart

@Composable
fun Cart(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    val cart = useCart(cartViewModel)

    when {
        cart.isLoading -> {
            CircularProgressIndicator()
        }

        cart.items.isEmpty() -> {
            // comming soon empty screen
            Text("Tu carrito esta vacio")
        }

        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = cart.items) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item.name)
                        Text(text = "x${item.quantity}")
                        Text(text = "$${item.price * item.quantity}")
                    }
                }

                item {
                    Text(
                        text = "Total: $${cart.totalPrice}",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
