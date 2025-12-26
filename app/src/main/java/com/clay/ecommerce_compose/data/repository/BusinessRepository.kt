package com.clay.ecommerce_compose.data.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.ProductInsertPayload
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
                context?.contentResolver?.openInputStream(uri).use {
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

    suspend fun getBusinessInfoByID(id: Int?): BusinessProfile? {
        val businessResult = supabase.from("businesses")
            .select {
                filter { id?.let { id -> eq("id", id) } }
            }
            .decodeSingleOrNull<BusinessProfile>()

        return businessResult
    }

    suspend fun getProductsByBusinessId(businessId: Int?): List<ProductPayload> {
        Log.e("BusinessRepositoryID", "getProductsByBusinessId: $businessId")

        val result = supabase.from("products")
            .select {
                filter {
                    businessId?.let { id ->
                        Log.e("BusinessRepositoryID", "Aplicando filtro con id: $id")
                        eq("business_id", id)
                    }
                }
            }
            .decodeList<ProductPayload>()

        Log.e("BusinessRepositoryID", "Productos encontrados: ${result.size}")

        result.forEach { product ->
            Log.e("BusinessRepositoryID", "Producto: id=${product.id}, name=${product.name}, businessId=${product.businessId}")
        }

        return result
    }

    suspend fun getBusinessById(businessId: String): BusinessProfile? {
        return supabase.from("businesses")
            .select {
                filter { eq("id", businessId) }
            }
            .decodeSingleOrNull()
    }

    suspend fun addProduct(product: ProductInsertPayload): Result<Unit> {
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
