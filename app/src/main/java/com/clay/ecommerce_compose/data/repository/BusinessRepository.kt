package com.clay.ecommerce_compose.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.ProductPayload
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class BusinessRepository(private val supabase: SupabaseClient, private val context: Context?) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun uploadProductImage(imageUri: String): Result<String> {
        return try {
            val uri = imageUri.toUri()

            val byteArray =
                context.contentResolver.openInputStream(uri).use {
                    it?.readBytes()
                } ?: throw Exception("No se pudo leer el archivo de imagen")

            val filename = "${System.currentTimeMillis()}.jpg"

            supabase.storage.from("product-image").upload(filename, byteArray)

            val publicUrl = supabase.storage.from("product-image").publicUrl(filename)

            Result.success(publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByBusinessId(businessId: Int?): List<ProductPayload> {
        return supabase.from("products")
            .select {
                filter { businessId?.let { eq("business_id", it) } }
            }
            .decodeList<ProductPayload>()
    }

    suspend fun getBusinessById(businessId: String): BusinessProfile? {
        return supabase.from("businesses")
            .select {
                filter { eq("id", businessId) }
            }
            .decodeSingleOrNull()
    }

    suspend fun addProduct(product: ProductPayload): Result<Unit> {
        return try {
            supabase.from("products").insert(product)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: Int, updates: ProductPayload): Result<Unit> {
        return try {
            supabase.from("products").update(updates) {
                filter { eq("id", productId) }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun disableProduct(productId: Int): Result<Unit> {
        return try {
            supabase.from("products").update(mapOf("is_active" to false)) {
                filter { eq("id", productId) }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }

}
