package com.clay.ecommerce_compose.ui.screens.client.cart

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.ui.components.client.cart.CartsScreen
import com.clay.ecommerce_compose.ui.components.client.cart.EmptyScreen
import com.clay.ecommerce_compose.utils.hooks.useCart

@Composable
fun Cart(navController: NavHostController, cartViewModel: CartViewModel) {
    val cart = useCart(cartViewModel)

    when {
        cart.isLoading -> {
            CircularProgressIndicator()
        }

        cart.items.isEmpty() -> {
            EmptyScreen(navController = navController)
        }

        else -> {
            CartsScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
    }
}
