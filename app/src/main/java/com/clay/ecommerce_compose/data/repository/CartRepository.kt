package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.domain.model.CartItemEntity
import com.clay.ecommerce_compose.ui.screens.client.cart.CartItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
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


class CartRepository(private val supabase: SupabaseClient) {
    private fun userId(): String = supabase.auth.currentUserOrNull()?.id
        ?: throw IllegalStateException("Usuario no autenticado")

    suspend fun getCart(): List<CartItem> {
        try {
            Log.d("CART_REPO", "Getting cart for user: ${userId()}")

            val response = supabase.from("cart_items_view")
                .select()
                {
                    filter { eq("user_id", userId()) }
                }
                .decodeList<CartItemEntity>()

            Log.d("CART_REPO", "getCart response: ${response.size} items")

            response.forEach { entity ->
                Log.d("CART_REPO", "Item: ${entity.name}, qty: ${entity.quantity}, stock: ${entity.stock}")
            }

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
        } catch (e: Exception) {
            Log.e("CART_REPO", "Error getting cart", e)
            throw e
        }
    }

    suspend fun addItem(item: CartItem) {
        val uid = userId()
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
                eq("user_id", userId())
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
                eq("user_id", userId())
                eq("product_id", notNullProductId)
            }
        }
    }

    suspend fun clearCart() {
        supabase.from("cart_items").delete {
            filter { eq("user_id", userId()) }
        }
    }
}
