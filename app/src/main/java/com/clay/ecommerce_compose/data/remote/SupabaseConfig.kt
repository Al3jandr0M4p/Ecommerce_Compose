package com.clay.ecommerce_compose.data.remote

import androidx.compose.runtime.Immutable
import com.clay.ecommerce_compose.BuildConfig   // ✅ ESTE ES EL CORRECTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

@Immutable
object SupabaseConfig {

    private const val supabaseUrl = BuildConfig.SUPABASE_URL
    private const val supabaseKey = BuildConfig.SUPABASE_KEY

    val client: SupabaseClient =
        createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
            install(Realtime)
            install(Functions)   // ✅ ya no dará error

            defaultSerializer = KotlinXSerializer(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    isLenient = true
                }
            )
        }
}
