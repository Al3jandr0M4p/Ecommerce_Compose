package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.domain.model.CartItemEntity
import com.clay.ecommerce_compose.domain.model.Order
import com.clay.ecommerce_compose.ui.screens.client.cart.CartItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemInsert(
    @SerialName("user_id")
    val userId: String,
    @SerialName("product_id")
    val productId: Int?,
    @SerialName("business_id")
    val businessId: Int,
    val quantity: Int
)

@Serializable
data class CartItemUpdate(
    val quantity: Int
)

@Serializable
data class CartItemResponse(
    val id: Int,
    @SerialName("product_id")
    val productId: Int,
    @SerialName("business_id")
    val businessId: Int,
    val quantity: Int
)

@Serializable
data class ApplyCouponRequest(
    val code: String? = null,
    val subtotal: Double = 0.0,
    @SerialName("totalQuantity")
    val totalQuantity: Int
)

@Serializable
data class ApplyCouponResponse(
    val code: String,
    val percentage: Double = 0.0,
    val discount: Double = 0.0,
)


class CartRepository(private val supabase: SupabaseClient, private val httpClient: HttpClient) {
    private fun userIdOrNull(): String? = supabase.auth.currentUserOrNull()?.id

    suspend fun applyCoupon(
        code: String?,
        subtotal: Double,
        totalQuantity: Int
    ): ApplyCouponResponse {

        return httpClient.post("https://6gszhspz-3002.use2.devtunnels.ms/api/payments/coupons/apply") {
            contentType(ContentType.Application.Json)
            setBody(
                ApplyCouponRequest(
                    code = code,
                    subtotal = subtotal,
                    totalQuantity = totalQuantity
                )
            )
        }.body()

    }

    suspend fun getActiveOrder(userId: String): Order? {
        val list = supabase.from("orders")
            .select {
                filter {
                    eq("user_id", userId)
                    isIn("status", listOf("pending", "paid", "waiting_delivery", "on_the_way"))
                }
                order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
            }
            .decodeList<Order>()

        return list.firstOrNull()
    }

    suspend fun getOrderInvoice(orderId: String): Order? {
        return try {
            supabase.from("orders")
                .select {
                    filter {
                        eq("id", orderId)
                    }
                }
                .decodeSingle<Order>()
        } catch (e: Exception) {
            Log.e("CART_REPO", "Error getting invoice for order: $orderId", e)
            null
        }
    }

    fun observeActiveOrder(userId: String): Flow<Order?> = flow {
        Log.d("ORDER_POLLING", "=== Flow started for user: $userId ===")

        while (true) {
            try {
                Log.d("ORDER_POLLING", "Checking active order...")
                val order = getActiveOrder(userId)
                Log.d("ORDER_POLLING", "Active order result: $order")
                emit(order)
            } catch (e: Exception) {
                Log.e("ORDER_POLLING", "Error checking active order", e)
                emit(null)
            }
            Log.d("ORDER_POLLING", "Waiting 6.5 seconds before next check...")
            delay(6500L)
        }
    }

    suspend fun getCart(): List<CartItem> {
        val uid = userIdOrNull() ?: return emptyList()

        val response = supabase.from("cart_items_view")
            .select()
            {
                filter { eq("user_id", uid) }
            }
            .decodeList<CartItemEntity>()

        return response.map { entity ->
            CartItem(
                id = entity.productId,
                businessName = entity.businessName,
                businessImg = entity.businessImg,
                businessId = entity.businessId,
                name = entity.name,
                price = entity.price,
                imageUrl = entity.imageUrl,
                quantity = entity.quantity,
                stock = entity.stock
            )
        }
    }

    suspend fun addItem(item: CartItem) {
        val uid = userIdOrNull() ?: ""

        val productId = item.id ?: run {
            Log.e("CART_REPO", "Product ID is null, cannot add to cart")
            throw IllegalArgumentException("Product ID cannot be null")
        }

        Log.d("CART_REPO", "Insert/Update cart uid=$uid product=${item.id}")

        try {
            // Primero intenta obtener el item existente
            val existingItems = supabase.from("cart_items")
                .select {
                    filter {
                        eq("user_id", uid)
                        eq("product_id", productId)
                    }
                }
                .decodeList<CartItemResponse>()

            if (existingItems.isNotEmpty()) {
                val newQuantity = existingItems.first().quantity + item.quantity

                supabase.from("cart_items").update(
                    CartItemUpdate(quantity = newQuantity)
                ) {
                    filter {
                        eq("user_id", uid)
                        eq("product_id", productId)
                    }
                }



                Log.d("CART_REPO", "Quantity updated to $newQuantity")
            } else {
                val cartItemInsert = CartItemInsert(
                    userId = uid,
                    productId = item.id,
                    businessId = item.businessId,
                    quantity = item.quantity
                )

                supabase.from("cart_items").insert(cartItemInsert)

                Log.d("CART_REPO", "Item inserted")
            }
        } catch (e: Exception) {
            Log.e("CART_REPO", "Error in addItem", e)
            throw e
        }
    }

    suspend fun updateQuantity(productId: Int?, quantity: Int) {
        val notNullProductId = productId ?: run {
            Log.e("CART_REPO", "Product ID is null, cannot update quantity")
            throw IllegalArgumentException("Product ID cannot be null")
        }

        supabase.from("cart_items").update(CartItemUpdate(quantity)) {
            filter {
                eq("user_id", userIdOrNull() ?: "")
                eq("product_id", notNullProductId)
            }
        }
    }

    suspend fun removeItem(productId: Int?) {
        val notNullProductId = productId ?: run {
            Log.e("CART_REPO", "Product ID is null, cannot remove item")
            throw IllegalArgumentException("Product ID cannot be null")
        }

        supabase.from("cart_items").delete {
            filter {
                eq("user_id", userIdOrNull() ?: "")
                eq("product_id", notNullProductId)
            }
        }
    }

    suspend fun clearCart() {
        supabase.from("cart_items").delete {
            filter { eq("user_id", userIdOrNull() ?: "") }
        }
    }
}
