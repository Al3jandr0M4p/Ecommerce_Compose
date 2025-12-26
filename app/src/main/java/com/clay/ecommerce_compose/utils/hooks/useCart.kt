package com.clay.ecommerce_compose.utils.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.clay.ecommerce_compose.ui.screens.client.cart.CartIntent
import com.clay.ecommerce_compose.ui.screens.client.cart.CartItem
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

data class CartController(
    val items: List<CartItem>,
    val totalPrice: Double,
    val isLoading: Boolean,
    val addItem: (CartItem) -> Unit,
    val removeItem: (String) -> Unit,
    val updateQuantity: (String, Int) -> Unit,
    val clearCart: () -> Unit
)

@Composable
fun useCart(viewModel: CartViewModel): CartController {
    val state by viewModel.state.collectAsState()

    val addItem: (CartItem) -> Unit = { item ->
        viewModel.handleIntent(intent = CartIntent.AddItem(item))
    }

    val removeItem: (String) -> Unit = { id ->
        viewModel.handleIntent(intent = CartIntent.RemoveItem(itemId = id.toInt()))
    }

    val updateQuantity: (String, Int) -> Unit = { id, qty ->
        viewModel.handleIntent(intent = CartIntent.UpdateQuantity(itemId = id.toInt(), quantity = qty))
    }

    val clearCart: () -> Unit = {
        viewModel.handleIntent(intent = CartIntent.ClearCart)
    }

    return CartController(
        items = state.items,
        totalPrice = state.totalPrice,
        isLoading = state.isLoading,
        addItem = addItem,
        removeItem = removeItem,
        updateQuantity = updateQuantity,
        clearCart = clearCart
    )
}
