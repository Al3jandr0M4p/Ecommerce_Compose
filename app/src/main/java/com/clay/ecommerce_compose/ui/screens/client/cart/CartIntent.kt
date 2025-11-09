package com.clay.ecommerce_compose.ui.screens.client.cart

sealed class CartIntent {
    data class AddItem(val item: CartItem) : CartIntent()
    data class RemoveItem(val itemId: String) : CartIntent()
    data class UpdateQuantity(val itemId: String, val quantity: Int) : CartIntent()
    data object ClearCart : CartIntent()
    data object LoadCart : CartIntent()
}
