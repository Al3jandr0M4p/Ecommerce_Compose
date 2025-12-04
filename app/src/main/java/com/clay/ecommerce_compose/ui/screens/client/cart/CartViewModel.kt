package com.clay.ecommerce_compose.ui.screens.client.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    fun handleIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.AddItem -> addItem(intent.item)
            is CartIntent.RemoveItem -> removeItem(intent.itemId)
            is CartIntent.UpdateQuantity -> updateQuantity(intent.itemId, intent.quantity)
            is CartIntent.ClearCart -> clearCart()
            is CartIntent.LoadCart -> loadCart()
        }
    }

    private fun addItem(item: CartItem) {
        viewModelScope.launch {
            val currentItems = _state.value.items.toMutableList()
            val existing = currentItems.find { it.id == item.id }

            if (existing != null) {
                val updated = existing.copy(quantity = existing.quantity + 1)
                currentItems[currentItems.indexOf(existing)] = updated
            } else {
                currentItems.add(item)
            }

            val total = currentItems.sumOf { it.price * it.quantity }
            _state.value = _state.value.copy(items = currentItems, totalPrice = total)
        }
    }

    private fun removeItem(itemId: String) {
        viewModelScope.launch {
            val updatedItems = _state.value.items.filterNot { it.id == itemId }
            val total = updatedItems.sumOf { it.price * it.quantity }
            _state.value = _state.value.copy(items = updatedItems, totalPrice = total)
        }
    }

    private fun updateQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            val updatedItems = _state.value.items.map {
                if (it.id == itemId) it.copy(quantity = quantity) else it
            }
            val total = updatedItems.sumOf { it.price * it.quantity }
            _state.value = _state.value.copy(items = updatedItems, totalPrice = total)
        }
    }

    private fun clearCart() {
        _state.value = CartState()
    }

    private fun loadCart() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            delay(300)
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}
