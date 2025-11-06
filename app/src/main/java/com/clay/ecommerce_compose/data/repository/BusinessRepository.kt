package com.clay.ecommerce_compose.data.repository

import com.clay.ecommerce_compose.domain.model.BusinessProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class BusinessRepository(private val supabase: SupabaseClient) {

    suspend fun getBusinessById(businessId: String): BusinessProfile? {
        return supabase.from("businesses")
            .select {
                filter { eq("id", businessId) }
            }
            .decodeSingleOrNull()
    }


}
