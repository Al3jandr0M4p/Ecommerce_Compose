package com.clay.ecommerce_compose.data.repository

import com.clay.ecommerce_compose.domain.model.BusinessProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from

class UserRepository(private val supabase: SupabaseClient) {

    suspend fun getAllBusiness(): List<BusinessProfile?> {
        return supabase.from("businesses")
            .select()
            .decodeList<BusinessProfile>()
    }

    suspend fun getUserInfoById(): UserInfo {
        return supabase.auth.retrieveUserForCurrentSession()
    }

}
