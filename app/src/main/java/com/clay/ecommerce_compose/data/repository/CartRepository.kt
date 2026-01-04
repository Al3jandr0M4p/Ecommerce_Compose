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
        return supabase.from("orders")
            .select {
                filter {
//                    Order::status isIn listOf("waiting_delivery", "on_the_way")
                    eq("user_id", userId)
                }
                order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                limit(1)
            }
            .decodeList<Order>()
            .firstOrNull()
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
