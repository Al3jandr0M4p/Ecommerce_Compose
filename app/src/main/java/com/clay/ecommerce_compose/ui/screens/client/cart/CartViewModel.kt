package com.clay.ecommerce_compose.ui.screens.client.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.CartRepository
import com.clay.ecommerce_compose.domain.model.Order
import com.clay.ecommerce_compose.domain.model.ProductPayload
import com.clay.ecommerce_compose.ui.components.client.header.NotificationItem
import com.clay.ecommerce_compose.ui.components.client.header.NotificationType
import com.clay.ecommerce_compose.ui.screens.client.cart.coupon.Coupon
import com.clay.ecommerce_compose.ui.screens.client.cart.coupon.CouponType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    private val _state = MutableStateFlow(value = CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(value = emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    val hasActiveDelivery: StateFlow<Boolean> = state.map { it.activeOrder != null }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(), initialValue = false
    )

    init {
        loadCart()
    }

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
            is CartIntent.ApplyCoupon -> applyCoupon(code = intent.code)
            is CartIntent.RemoveCoupon -> removeCoupon()
            is CartIntent.SelectPaymentMethod -> selectPaymentMethod(method = intent.method)
        }
    }

    fun addNotification(notification: NotificationItem) {
        _notifications.update { current ->
            current + notification
        }
    }

    fun removeNotification(id: String) {
        _notifications.update { current ->
            current.filterNot { it.id == id }
        }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }

    // Ejemplo de stock bajo
    fun checkLowStock(products: List<ProductPayload>) {
        val lowStockNotifs = products
            .filter { it.stock in 1..5 }
            .map {
                NotificationItem(
                    id = "stock-${it.id}",
                    title = "Stock bajo",
                    message = "${it.name} tiene solo ${it.stock} unidades restantes",
                    type = NotificationType.STOCK_LOW
                )
            }
        _notifications.update { current ->
            current.filterNot { it.type == NotificationType.STOCK_LOW } + lowStockNotifs
        }
    }

    fun onOrderCreated(order: Order) {
        _state.update { it.copy(activeOrder = order) }
        clearCart()
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

            if (quantity < 1) return@launch
            if (quantity > item.quantity && item.stock <= 0) return@launch

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
            runCatching {
                cartRepository.clearCart()
            }.onSuccess {
                _state.update { current ->
                    current.copy(
                        items = emptyList(),
                        subTotal = 0.0,
                        deliveryFee = 0.0,
                        serviceFee = 0.0,
                        couponDiscount = 0.0,
                        totalPrice = 0.0,
                        appliedCoupon = null
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }


    private fun selectPaymentMethod(method: String) {
        _state.value = _state.value.copy(selectedPaymentMethod = method)
    }

    private fun applyCoupon(code: String) {
        viewModelScope.launch {
            val subTotal = _state.value.subTotal
            val totalQty = _state.value.items.sumOf { it.quantity }

            runCatching {
                cartRepository.applyCoupon(
                    code = code,
                    subtotal = subTotal,
                    totalQuantity = totalQty
                )
            }.onSuccess { result ->
                val couponType =
                    if (result.percentage > 0) CouponType.PERCENTAGE else CouponType.FIXED


                _state.value = calculateTotals(
                    items = _state.value.items,
                    couponDiscount = result.discount
                ).copy(
                    appliedCoupon = Coupon(
                        code = result.code,
                        type = couponType,
                        value = if (couponType == CouponType.PERCENTAGE)
                            result.percentage
                        else
                            result.discount
                    ),
                    couponError = null
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    couponError = it.message,
                    couponDiscount = 0.0,
                    appliedCoupon = null
                )
            }
        }
    }

    private fun removeCoupon() {
        _state.value = calculateTotals(
            items = _state.value.items,
            couponDiscount = 0.0
        ).copy(
            appliedCoupon = null,
            couponError = null
        )
    }

    private fun updateLocalState(items: List<CartItem>) {
        _state.value = calculateTotals(items)
    }

    private fun calculateTotals(items: List<CartItem>, couponDiscount: Double = 0.0): CartState {
        val subtotal = items.sumOf { it.price * it.quantity }

        val deliveryFee = if (items.isNotEmpty()) 1550.0 else 0.0
        val serviceFee = if (items.isNotEmpty()) 2500.0 else 0.0
        val itbis = if (items.isNotEmpty()) subtotal * 0.18 else 0.0

        val total = (subtotal + deliveryFee + serviceFee + itbis) - couponDiscount

        return _state.value.copy(
            items = items,
            subTotal = subtotal,
            deliveryFee = deliveryFee,
            serviceFee = serviceFee,
            couponDiscount = couponDiscount,
            totalPrice = total.coerceAtLeast(0.0)
        )
    }
}
