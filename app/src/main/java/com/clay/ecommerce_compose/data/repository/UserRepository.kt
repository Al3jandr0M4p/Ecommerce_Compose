package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.Profile
import com.clay.ecommerce_compose.domain.model.User
import com.clay.ecommerce_compose.domain.model.UserToInsert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class UserRepository(private val supabase: SupabaseClient) {

    // Obtener todos los negocios
    suspend fun getAllBusiness(): List<BusinessProfile> {
        return supabase
            .from("businesses")
            .select()
            .decodeList()
    }

    // Obtener todos los usuarios desde profiles
    suspend fun getAllUsers(): List<Profile> {
        return supabase
            .from("profiles")
            .select()
            .decodeList()
    }

    // Obtener info del usuario autenticado
    suspend fun getUserInfoById(): UserInfo {
        return supabase.auth.retrieveUserForCurrentSession()
    }

    // Crear usuario usando Edge Function segura
    suspend fun createUser(user: UserToInsert) {
        try {
            supabase.functions.invoke(
                "admin_create_user",
                body = buildJsonObject {
                    put("email", JsonPrimitive(user.email))
                    put("password", JsonPrimitive(user.password))
                    put("username", JsonPrimitive(user.userName))
                    put("role_name", JsonPrimitive(user.roleName))
                }
            )

            Log.d("UserRepository", "✅ Usuario creado correctamente: ${user.email}")

        } catch (e: Exception) {
            Log.e("UserRepository", "❌ Error creando usuario", e)
            throw e
        }
    }

//    suspend fun updateUser(user: User) {
//        supabase.from("profiles")
//            .update(
//                buildJsonObject {
//                    put("name", JsonPrimitive(user.name))
//                    put("email", JsonPrimitive(user.email))
//                    put("role", JsonPrimitive(user.role))
//                }
//            ) {
//                filter {
//                    eq("id", user.id)
//                }
//            }
//    }
//
//
//
//    suspend fun deleteUser(userId: String) {
//        supabase.from("profiles")
//            .delete {
//                filter {
//                    eq("id", userId)
//                }
//            }
//    }
}

