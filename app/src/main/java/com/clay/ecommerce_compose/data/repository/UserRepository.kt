package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.Profile
import com.clay.ecommerce_compose.domain.model.RoleIdResponse
import com.clay.ecommerce_compose.domain.model.UserToInsert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class UserRepository(private val supabase: SupabaseClient) {

    suspend fun getAllBusiness(): List<BusinessProfile?> {
        return supabase.from("businesses")
            .select()
            .decodeList<BusinessProfile>()
    }

    suspend fun getAllUsers(): List<Profile> {
        return supabase.from("profiles")
            .select()
            .decodeList<Profile>()
    }

    suspend fun getUserInfoById(): UserInfo {
        return supabase.auth.retrieveUserForCurrentSession()
    }

    suspend fun createUser(user: UserToInsert) {
        try {
            // 1️⃣ Crear usuario en Auth
            val authResult = supabase.auth.signUpWith(Email) {
                email = user.email
                password = user.password
                data = buildJsonObject {
                    put("username", JsonPrimitive(user.userName))
                }
            }

            val userId = authResult?.user?.id
                ?: throw IllegalStateException("No se pudo obtener el ID del usuario")

            // 2️⃣ Obtener role_id
            val role = supabase.from("roles")
                .select(Columns.list("id")) {
                    filter { eq("name", user.roleName) }
                }
                .decodeSingle<RoleIdResponse>()

            // 3️⃣ Insertar / actualizar profile
            supabase.from("profiles").upsert(
                buildJsonObject {
                    put("id", JsonPrimitive(userId))
                    put("role_id", JsonPrimitive(role.id))
                }
            )

            Log.d("UserRepository", "Usuario creado correctamente: ${user.email}")

        } catch (e: Exception) {
            Log.e("UserRepository", "Error creando usuario", e)
            throw e
        }
    }
}