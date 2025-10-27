package com.clay.ecommerce_compose.data.repository

import android.net.Uri
import android.util.Log
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.Profile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
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

    suspend fun signUpBusiness(
        email: String,
        password: String,
        name: String,
        logo: Uri?,
        logoByteArray: ByteArray?,
        direccion: String,
        horarioApertura: String,
        horarioCierre: String,
        telefono: String,
        description: String,
        hasDelivery: Boolean,
        category: String
    ): BusinessProfile? {
        try {
            val result = supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("username", JsonPrimitive(name))
                }
            }

            val userId = result?.id ?: throw IllegalStateException("No se pudo crear el usuario")

            var logoUrl: String? = null

            if (logo != null && logoByteArray != null) {
                val fileExtension = logo.lastPathSegment?.substringAfterLast('.', "jpg")
                val path = "business_logo/${userId}_${System.currentTimeMillis()}.$fileExtension"

                supabase.storage.from("logos").upload(path, logoByteArray)

                logoUrl = supabase.storage.from("logos").publicUrl(path)

                Log.d("AuthRepository", "Logo subido a $logoUrl")
            }

            val newBusinessProfile = supabase.from("businesses").insert(
                buildJsonObject {
                    put("owner_id", JsonPrimitive(userId))
                    put("name", JsonPrimitive(name))
                    put("address", JsonPrimitive(direccion))
                    put("opening_time", JsonPrimitive(horarioApertura))
                    put("closing_time", JsonPrimitive(horarioCierre))
                    put("phone", JsonPrimitive(telefono))
                    put("description", JsonPrimitive(description))
                    put("has_delivery", JsonPrimitive(hasDelivery))
                    put("category", JsonPrimitive(category))
                    logoUrl?.let { put("logo_url", JsonPrimitive(it)) }
                }
            ).decodeSingle<BusinessProfile>()

            Log.d("AuthRepository", "Negocio registrado: $newBusinessProfile")
            return newBusinessProfile

        } catch (e: Exception) {
            supabase.auth.currentUserOrNull()?.id.let {

            }

            Log.e("AuthRepository", "Error al registrar el negocio", e)
            throw e
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
