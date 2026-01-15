package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.domain.model.Business
import com.clay.ecommerce_compose.domain.model.UserWithRole
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.JsonPrimitive

class NegocioRepository(private val supabase: SupabaseClient) {

    suspend fun getBusinesses(): List<Business> {
        return supabase.from("businesses")
            .select()
            .decodeList()
    }

    suspend fun getUsersWithBusinessRole(): List<UserWithRole> {
        return try {
            // ✅ Usar la función RPC que hace el JOIN con auth.users
            val users = supabase.postgrest
                .rpc("get_business_users")
                .decodeList<UserWithRole>()

            Log.d("NegocioRepository", "✅ Usuarios con email encontrados: ${users.size}")
            users.forEach { user ->
                Log.d("NegocioRepository", "Usuario: ${user.email} (ID: ${user.id.take(8)}...)")
            }

            users
        } catch (e: Exception) {
            Log.e("NegocioRepository", "❌ Error al obtener usuarios: ${e.message}", e)
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createBusiness(
        ownerId: String,
        name: String,
        phone: String,
        category: String
    ) {
        supabase.from("businesses").insert(
            buildJsonObject {
                put("owner_id", JsonPrimitive(ownerId))
                put("name", JsonPrimitive(name))
                put("phone", JsonPrimitive(phone))
                put("category", JsonPrimitive(category))
                put("status", JsonPrimitive("Aprobado"))
                put("has_delivery", JsonPrimitive(false))
                put("rnc", JsonPrimitive(generateRnc()))
                put("ncf", JsonPrimitive(generateNcf()))
                put("opening_time", JsonPrimitive("08:00"))
                put("closing_time", JsonPrimitive("18:00"))
                put("description", JsonPrimitive(""))
                put("logo_url", JsonPrimitive(""))
            }
        )
    }

    suspend fun updateBusiness(
        id: Int,
        name: String,
        phone: String,
        category: String
    ) {
        supabase.from("businesses").update(
            buildJsonObject {
                put("name", JsonPrimitive(name))
                put("phone", JsonPrimitive(phone))
                put("category", JsonPrimitive(category))
            }
        ) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteBusiness(id: Int) {
        supabase.from("businesses").delete {
            filter { eq("id", id) }
        }
    }

    private fun generateNcf(): String {
        return "B" + (1..10).joinToString("") { (0..9).random().toString() }
    }

    private fun generateRnc(): String {
        return (1..9).joinToString("") { (0..9).random().toString() }
    }
}