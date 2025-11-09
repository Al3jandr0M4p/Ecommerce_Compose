package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@Composable
fun ShoppingCart(navController: NavHostController, cartViewModel: CartViewModel) {
    val state by cartViewModel.state.collectAsState()

    Box(contentAlignment = Alignment.TopEnd) {
        IconButton(onClick = { navController.navigate("cart") }) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "User cart",
                modifier = Modifier.size(28.dp)
            )
        }

        if (state.items.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .offset(x = (-6).dp, y = 6.dp)
                    .size(20.dp)
                    .background(color = Color(0xFF06C167), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.items.size.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
