package com.clay.ecommerce_compose.ui.screens.client.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.CartRepository
import com.clay.ecommerce_compose.data.repository.NotificationRepository
import com.clay.ecommerce_compose.domain.model.Order
import com.clay.ecommerce_compose.domain.model.ProductPayload
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import com.clay.ecommerce_compose.ui.components.client.header.NotificationItem
import com.clay.ecommerce_compose.ui.components.client.header.NotificationType
import com.clay.ecommerce_compose.ui.screens.client.cart.coupon.Coupon
import com.clay.ecommerce_compose.ui.screens.client.cart.coupon.CouponType
import com.clay.ecommerce_compose.ui.screens.register.delivery.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val notificationRepository: NotificationRepository,
    private val getCurrentUserSession: GetCurrentUserSessionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(value = CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(value = emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _invoiceState = MutableStateFlow<Order?>(null)
    val invoiceState: StateFlow<Order?> = _invoiceState.asStateFlow()

    val hasActiveDelivery: StateFlow<Boolean> = state.map { it.activeOrder != null }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    init {
        viewModelScope.launch {
            delay(1000)
            val session = getCurrentUserSession().firstOrNull() ?: return@launch

            Log.d("CartViewModel", "User session found: ${session.id}")

            loadCart()
            loadNotifications()
            observeNotifications()

            Log.d("CartViewModel", "About to start observing active order for user: ${session.id}")
            observeActiveOrder(session.id)
        }
    }

    fun observeActiveOrder(userId: String) {
        Log.d("CartViewModel", "observeActiveOrder called for user: $userId")

        viewModelScope.launch {
            try {
                Log.d("CartViewModel", "Starting to collect active order flow...")

                cartRepository.observeActiveOrder(userId)
                    .collect { order ->
                        Log.d("ORDER_SYNC", "Active order received in ViewModel: $order")

                        _state.update {
                            it.copy(activeOrder = order)
                        }
                    }

            } catch (e: Exception) {
                Log.e("CartViewModel", "Error observing active order", e)
            }
        }
    }

    fun refreshActiveOrder() {
        viewModelScope.launch {
            val session = getCurrentUserSession().firstOrNull() ?: return@launch
            loadActiveOrder(session.id)
        }
    }


    private fun loadActiveOrder(userId: String) {
        viewModelScope.launch {
            try {
                val order = cartRepository.getActiveOrder(userId)
                Log.d("ORDER_SYNC", "Active order loaded: $order")
                _state.update {
                    it.copy(activeOrder = order)
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error loading active order", e)
            }
        }
    }

    fun handleIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.AddItem -> addItem(item = intent.item)
            is CartIntent.RemoveItem -> removeItem(itemId = intent.itemId)
            is CartIntent.UpdateQuantity -> updateQuantity(
                itemId = intent.itemId,
                quantity = intent.quantity
            )
            is CartIntent.GetOrderInvoice -> getOrderInvoice(intent.orderId)

            is CartIntent.ClearCart -> clearCart()
            is CartIntent.LoadCart -> loadCart()
            is CartIntent.ApplyCoupon -> applyCoupon(code = intent.code)
            is CartIntent.RemoveCoupon -> removeCoupon()
            is CartIntent.SelectPaymentMethod -> selectPaymentMethod(method = intent.method)
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            try {
                val list = notificationRepository.getNotifications()
                _notifications.value = list
                Log.d("CartViewModel", "Loaded ${list.size} notifications")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error loading notifications", e)
            }
        }
    }

    private fun getOrderInvoice(orderId: String) {
        viewModelScope.launch {
            try {
                val order = cartRepository.getOrderInvoice(orderId)
                _invoiceState.value = order
                Log.d("CartViewModel", "Invoice loaded for order $orderId")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error loading invoice", e)
            }
        }
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            try {
                notificationRepository.listenNotifications()
                    .collect { notification ->
                        _notifications.update { current ->
                            if (current.none { it.id == notification.id }) {
                                val updated = current + notification
                                Log.d(
                                    "CartViewModel",
                                    "Notifications updated. Total ${updated.size}"
                                )
                                updated
                            } else {
                                Log.d(
                                    "CartViewModel",
                                    "Notification alredy exists: ${notification.id}"
                                )
                                current
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error listening to notifications", e)
            }
        }
    }

    fun addNotification(notification: NotificationItem) {
        viewModelScope.launch {
            try {

                val realId = notificationRepository.insertNotification(notification)

                _notifications.update { current ->
                    if (current.none { it.id == notification.id }) {
                        current + notification
                    } else {
                        current
                    }
                }

                Log.d("CartViewModel", "notification added: $realId")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error adding notification", e)
            }
        }
    }

    fun removeNotification(id: String) {
        viewModelScope.launch {
            try {

                _notifications.update { current ->
                    current.filterNot { it.id == id }
                }

                notificationRepository.deleteNotification(id)

                Log.d("CartViewModel", "Notification removed: $id")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error removing notification", e)

                loadNotifications()
            }
        }
    }

//    fun markNotificationAsRead(id: String) {
//        viewModelScope.launch {
//            try {
//                notificationRepository.markAsRead(id)
//            } catch (e: Exception) {
//                Log.e("CartViewModel", "Error marking notification as read", e)
//            }
//        }
//    }

    fun checkLowStock(products: List<ProductPayload>) {
        viewModelScope.launch {
            try {
                val lowStockProduct = products.filter { it.stock in 1..5 }

                lowStockProduct.forEach { product ->
                    val notification = NotificationItem(
                        id = "",
                        title = "Stock bajo",
                        message = "${product.name} tiene solo ${product.stock} unidades restantes",
                        type = NotificationType.STOCK_LOW
                    )

                    notificationRepository.insertNotification(notification)
                }

                loadNotifications()

                Log.d("CartViewModel", "Low Stock notification sent: ${lowStockProduct.size}")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error checking low stock", e)
            }
        }
    }

    fun onOrderCreated(order: Order) {
        _state.update { it.copy(activeOrder = order) }
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
                addNotification(
                    NotificationItem(
                        id = "",
                        title = "Producto agregado",
                        message = "${item.name} fue agregado al carrito exitosamente",
                        type = NotificationType.ORDER_STATUS
                    )
                )
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
                cartRepository.updateQuantity(productId = itemId, quantity)
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
                cartRepository.removeItem(productId = itemId)
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
        if (code.isBlank()) {
            removeCoupon()
            return
        }

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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            try {
                notificationRepository.cleanup()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error cleaning up notifications", e)
            }
        }
    }
}
