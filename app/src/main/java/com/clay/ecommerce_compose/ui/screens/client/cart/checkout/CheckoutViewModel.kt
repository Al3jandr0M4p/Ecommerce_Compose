package com.clay.ecommerce_compose.ui.screens.client.cart.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.OrderRepository
import com.clay.ecommerce_compose.domain.model.Order
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.utils.Orders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    fun handleIntent(intent: CheckoutIntent, cartViewModel: CartViewModel) {
        when (intent) {
            is CheckoutIntent.PlaceOrder -> placeOrder(
                paymentMethod = intent.paymentMethod,
                couponCode = intent.couponCode,
                cartViewModel = cartViewModel
            )

            CheckoutIntent.ClearError -> clearError()
        }
    }

    private fun placeOrder(paymentMethod: String, couponCode: String?, cartViewModel: CartViewModel) {
        viewModelScope.launch {
            Log.d("CHECKOUT_VM", "PlaceOrder called. PaymentMethod=$paymentMethod, couponCode=$couponCode")
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val result = orderRepository.createOrder(
                    paymentMethod = paymentMethod,
                    couponCode = couponCode
                )

                Log.d("CHECKOUT_VM", "OrderRepository returned result: $result")

                _state.update {
                    it.copy(
                        isLoading = false,
                        orderResult = result
                    )
                }

                val order = Order(
                    id = result.orderId,
                    status = Orders.pending,
                    paymentMethod = paymentMethod
                )

                Log.d("CHECKOUT_VM", "Notifying CartViewModel with Order: $order")
                cartViewModel.onOrderCreated(order)
            } catch (e: Exception) {
                Log.e("CHECKOUT_VM", "Error placing order", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

}