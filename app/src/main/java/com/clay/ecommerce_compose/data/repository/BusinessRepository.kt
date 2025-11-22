package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class BusinessRepository(private val supabase: SupabaseClient) {

    suspend fun getBusinessById(businessId: String): BusinessProfile? {
        return supabase.from("businesses")
            .select {
                filter { eq("id", businessId) }
            }
            .decodeSingleOrNull()
    }

    suspend fun addProduct(businessId: String, product: Map<String, Any?>): Result<Unit> {
        return try {
            val payload = product.toMutableMap().also {
                it["business_id"] = businessId
            }
            val response = supabase.from("products").insert(payload)
            Log.d("DEBUG_INSERT", response.toString())

            if (response.data != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Insert failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: Int, updates: Map<String, Any?>): Result<Unit> {
        return try {
            val response = supabase.from("products").update(updates) {
                filter { eq("id", productId) }
            }

            if (response.data != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Update failed 'updates'"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun disableProduct(productId: Int): Result<Unit> {
        return try {
            val response = supabase.from("products").update(mapOf("is_active" to false)) {
                filter { eq("id", productId) }
            }

            if (response.data != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Update failed 'is_active'"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }

}
