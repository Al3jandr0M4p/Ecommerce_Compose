package com.clay.ecommerce_compose.data.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

class AuthRepository(private val supabase: SupabaseClient) {

    suspend fun signUp(email: String, password: String, name: String, lastname: String): Profile? {
        return try {
            val fullName = "$name $lastname"
            val result = supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("username", JsonPrimitive(fullName))
                }
            }

            if (result?.id != null) {
                val newUserProfile = supabase.from("profiles").insert(
                    buildJsonObject {
                        put("id", JsonPrimitive(result.id))
                        put("role_id", JsonPrimitive(1))
                    }
                ) {
                    select(Columns.list("id", "role_id"))
                }.decodeSingle<Profile>()

                if (newUserProfile.roleId != null) {
                    val roleNameObject = supabase.from("roles")
                        .select(Columns.list("name")) {
                            filter { eq("id", newUserProfile.roleId) }
                        }
                        .decodeSingleOrNull<JsonObject>()
                    val roleName = roleNameObject?.get("name")?.jsonPrimitive?.contentOrNull

                    newUserProfile.roleName = roleName
                }

                Log.d("AuthRepository", "Usuario registrado: $newUserProfile")
                return newUserProfile
            }

            return null
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en signUp", e)
            null
        }
    }

    suspend fun signIn(email: String, password: String): Profile? {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val user = supabase.auth.currentUserOrNull()
            if (user != null) {
                val profileResponse = supabase.from("profiles")
                    .select(Columns.list("id", "role_id")) {
                        filter { eq("id", user.id) }
                    }
                    .decodeSingleOrNull<Profile>()

                if (profileResponse?.roleId != null) {
                    val roleNameObject = supabase.from("roles")
                        .select(Columns.list("name")) {
                            filter { eq("id", profileResponse.roleId) }
                        }
                        .decodeSingleOrNull<JsonObject>()

                    val roleName = roleNameObject?.get("name")?.jsonPrimitive?.contentOrNull

                    profileResponse.roleName = roleName
                }

                return profileResponse
            } else null
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en signIn", e)
            null
        }
    }

    suspend fun getCurrentUserProfile(): Profile? {
        try {
            val user = supabase.auth.currentUserOrNull() ?: return null

            val profileResponse = supabase.from("profiles")
                .select(Columns.list("id", "role_id")) {
                    filter { eq("id", user.id) }
                }
                .decodeSingleOrNull<Profile>()

            if (profileResponse?.roleId != null) {
                val roleNameObject = supabase.from("roles")
                    .select(Columns.list("name")) {
                        filter { eq("id", profileResponse.roleId) }
                    }
                    .decodeSingleOrNull<JsonObject>()

                val roleName = roleNameObject?.get("name")?.jsonPrimitive?.contentOrNull

                profileResponse.roleName = roleName
            }
            Log.d("AuthRepository", "Perfil del usuario: $profileResponse")
            return profileResponse
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener el perfil del usuario", e)
            supabase.auth.signOut()
            return null
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
