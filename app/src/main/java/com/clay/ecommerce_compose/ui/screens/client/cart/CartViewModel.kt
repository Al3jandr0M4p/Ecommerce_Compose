package com.clay.ecommerce_compose.ui.screens.client.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    private val _state = MutableStateFlow(value = CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    fun handleIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.AddItem -> addItem(item = intent.item)
            is CartIntent.RemoveItem -> removeItem(itemId = intent.itemId)
            is CartIntent.UpdateQuantity -> updateQuantity(
                itemId = intent.itemId,
                quantity = intent.quantity
            )

            is CartIntent.ClearCart -> clearCart()
            is CartIntent.LoadCart -> loadCart()
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            Log.d("CART_VM", "Loading cart...")

            runCatching {
                cartRepository.getCart()
            }.onSuccess { items ->
                Log.d("CART_VM", "Cart loaded: ${items.size} items")

                items.forEach {
                    Log.d("CART_VM", "Producto ${it.name} stock=${it.stock} qty=${it.quantity}")
                }

                _state.value = calculateTotals(items).copy(isLoading = false)
            }.onFailure { e ->
                Log.e("CART_VM", "Error loading cart", e)
                _state.value = CartState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun addItem(item: CartItem) {
        viewModelScope.launch {
            Log.d("CART_VM", "AddItem -> product=${item.id}")

            runCatching {
                cartRepository.addItem(item)
            }.onSuccess {
                Log.d("CART_VM", "Item insertado en DB")
                loadCart()
            }.onFailure {
                Log.e("CART_VM", "Error insertando item", it)
            }
        }
    }


    private fun updateQuantity(itemId: Int?, quantity: Int) {
        viewModelScope.launch {
            val item = _state.value.items.find { it.id == itemId } ?: return@launch

            if (quantity > item.stock) return@launch

            val newItems = _state.value.items.map {
                if (it.id == itemId) it.copy(quantity = quantity) else it
            }

            updateLocalState(newItems)

            runCatching {
                cartRepository.updateQuantity(itemId, quantity)
            }.onFailure {
                loadCart()
            }
        }
    }

    private fun removeItem(itemId: Int?) {
        viewModelScope.launch {
            val newItems = _state.value.items.filterNot { it.id == itemId }

            updateLocalState(newItems)

            runCatching {
                cartRepository.removeItem(itemId)
            }.onFailure {
                loadCart()
            }
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            _state.value = CartState(isLoading = true)

            runCatching {
                cartRepository.clearCart()
            }.onSuccess {
                _state.value = CartState()
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun updateLocalState(items: List<CartItem>) {
        _state.value = calculateTotals(items)
    }

    private fun calculateTotals(items: List<CartItem>): CartState {
        val subtotal = items.sumOf { it.price * it.quantity }

        val deliveryFee = if (items.isNotEmpty()) 1550.0 else 0.0
        val serviceFee = if (items.isNotEmpty()) 2500.0 else 0.0
        val itbis = if (items.isNotEmpty()) subtotal * 0.18 else 0.0

        val total = subtotal + deliveryFee + serviceFee + itbis

        return _state.value.copy(
            items = items,
            subTotal = subtotal,
            deliveryFee = deliveryFee,
            serviceFee = serviceFee,
            totalPrice = total
        )
    }
}
