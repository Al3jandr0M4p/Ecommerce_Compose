package com.clay.ecommerce_compose.data.repository

import android.net.Uri
import android.util.Log
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.Profile
import com.clay.ecommerce_compose.domain.model.RoleIdResponse
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

    data class InternalUserProfile(
        val id: String,
        val roleName: String?,
        val businessId: String? = null
    )

    private suspend fun getBusinessByOwnerId(ownerId: String): String? {
        return try {
            val result = supabase.from("businesses")
                .select(Columns.list("id")) {
                    filter { eq("owner_id", ownerId) }
                }
                .decodeList<JsonObject>()

            return result.firstOrNull()?.get("id")?.jsonPrimitive?.contentOrNull
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener businessId por ownerId", e)
            null
        }
    }

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
            Log.d("AuthRepository", "Usuario autenticado (ID): ${user?.id}")

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

                    Log.d("AuthRepository", "Rol obtenido en SigIn $roleName para ID: ${user.id}")
                }
                Log.d("AuthRepository", "Usuario logueado: $profileResponse")
                return profileResponse
            } else {
                Log.e("AuthRepository", "Error: La sesión es válida pero el usuario es null")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en signIn", e)
            null
        }
    }

    suspend fun getCurrentUserProfile(): InternalUserProfile? {
        try {
            val user = supabase.auth.currentUserOrNull() ?: return null

            val profileResponse = supabase.from("profiles")
                .select(Columns.list("id", "role_id")) {
                    filter { eq("id", user.id) }
                }
                .decodeSingleOrNull<Profile>()

            var roleName: String? = null
            if (profileResponse?.roleId != null) {
                val roleNameObject = supabase.from("roles")
                    .select(Columns.list("name")) {
                        filter { eq("id", profileResponse.roleId) }
                    }
                    .decodeSingleOrNull<JsonObject>()

                roleName = roleNameObject?.get("name")?.jsonPrimitive?.contentOrNull
            }

            var businessId: String? = null
            if (roleName == "negocio") {
                businessId = getBusinessByOwnerId(user.id)
            }

            val finalProfile = InternalUserProfile(
                id = user.id,
                roleName = roleName,
                businessId = businessId
            )

            Log.d("AuthRepository", "Perfil del usuario: $finalProfile")
            return finalProfile
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
        horarioApertura: String,
        horarioCierre: String,
        category: String,
        rnc: String,
        ncf: String,
        telefono: String,
        hasDelivery: Boolean
    ): BusinessProfile? {
        var sessionUserId: String? = null
        var logoPath: String? = null

        try {
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = buildJsonObject {
                        put("username", JsonPrimitive(name))
                    }
                }
            } catch (authException: Exception) {
                Log.e("AuthRepository", "Fallo en signUpWith", authException)
                throw authException
            }

            sessionUserId = supabase.auth.currentUserOrNull()?.id
                ?: throw IllegalStateException("No hay usuario autenticado despues del registro")

            var logoUrl: String? = null

            if (logo != null && logoByteArray != null) {
                val fileExtension = logo.lastPathSegment?.substringAfterLast('.', "jpg")
                logoPath =
                    "business_logo/${sessionUserId}_${System.currentTimeMillis()}.$fileExtension"

                supabase.storage.from("logos").upload(logoPath, logoByteArray)
                logoUrl = supabase.storage.from("logos").publicUrl(logoPath)

                Log.d("AuthRepository", "Logo subido a $logoUrl")
            }

            val newBusinessProfile = supabase.from("businesses").insert(
                buildJsonObject {
                    put("owner_id", JsonPrimitive(sessionUserId))
                    put("name", JsonPrimitive(name))
                    put("opening_time", JsonPrimitive(horarioApertura))
                    put("closing_time", JsonPrimitive(horarioCierre))
                    put("phone", JsonPrimitive(telefono))
                    put("has_delivery", JsonPrimitive(hasDelivery))
                    put("category", JsonPrimitive(category))
                    put("rnc", JsonPrimitive(rnc))
                    put("ncf", JsonPrimitive(ncf))
                    logoUrl?.let { put("logo_url", JsonPrimitive(it)) }
                }
            ) {
                select()
            }.decodeSingle<BusinessProfile>()

            val businessRole = supabase.from("roles")
                .select(Columns.raw("id")) {
                    filter { eq("name", "negocio") }
                }
                .decodeSingleOrNull<RoleIdResponse>()

            val roleId = businessRole?.id
                ?: throw IllegalStateException("El rol 'negocio' no existe en la base de datos. Verifica la tabla roles.")

            supabase.from("profiles").upsert(
                buildJsonObject {
                    put("id", JsonPrimitive(sessionUserId))
                    put("role_id", JsonPrimitive(roleId))
                }
            )

            Log.d("AuthRepository", "Negocio registrado y rol actualizado $newBusinessProfile")
            return newBusinessProfile

        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en signUpBusiness", e)

            if (sessionUserId != null && logoPath != null) {
                try {
                    supabase.storage.from("logos").delete(listOf(logoPath))
                    Log.d("AuthRepository", "Logo de negocio eliminado tras fallo.")
                } catch (removeException: Exception) {
                    Log.e(
                        "AuthRepository",
                        "Advertencia: Fallo al eliminar el logo subido.",
                        removeException
                    )
                }
            }

            supabase.auth.signOut()
            throw e
        }
    }
}
