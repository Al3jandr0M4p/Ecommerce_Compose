package com.clay.ecommerce_compose.data.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class AuthRepository(private val supabase: SupabaseClient) {

    suspend fun signUp(email: String, password: String, username: String): Profile? {
        return try {
            val result = supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("username", JsonPrimitive(username))
                }
            }

            val session = supabase.auth.currentSessionOrNull()
            Log.d("AuthRepository", "Usuario registrado: $result")

            Profile(id = result?.id ?: "", username = username)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en signUp", e)
            null
        }
    }

    suspend fun signIn(email: String, password: String): Profile? {
        return try {
            val result = supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val user = supabase.auth.currentUserOrNull()
            if (user != null) {
                val profile = supabase.from("profiles")
                    .select(columns = Columns.list("id", "name")) {
                        filter { eq("id", user.id) }
                    }
                    .decodeSingle<Profile>()
                profile
            } else null
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en signIn", e)
            null
        }
    }

    suspend fun signOut() {
        try {
            supabase.auth.signOut()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al cerrar la session ${e.message}")
        }
    }

}
