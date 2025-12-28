package com.clay.ecommerce_compose.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Wallet(
    val balance: Double
)

@Serializable
data class WalletDTO(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val balance: Double,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

class WalletRepository(private val supabase: SupabaseClient) {
    private fun userId() = supabase.auth.currentUserOrNull()?.id ?: error("Usuario no autenticado")

    suspend fun getWallet(): Wallet {
        val result =
            supabase.from("wallets").select {
                filter {
                    eq("user_id", userId())
                }
            }.decodeSingle<WalletDTO>()

        return Wallet(balance = result.balance)
    }

    suspend fun addBalance(amount: Double) {
        supabase.from("wallets").update(mapOf("balance" to "balance + $amount")) {
            filter { eq("user_id", userId()) }
        }

        supabase.from("wallet_transactions").insert(
            mapOf(
                "user_id" to userId(),
                "amount" to amount,
                "type" to "credit",
                "reason" to "Recarga de cartera"
            )
        )
    }
    suspend fun deductBalance(amount: Double) {
        supabase.from("wallets").update(mapOf("balance" to "balance - $amount")) {
            filter { eq("user_id", userId()) }
        }

        supabase.from("wallet_transactions").insert(
            mapOf(
                "user_id" to userId(),
                "amount" to amount,
                "type" to "debit",
                "reason" to "Pago de compra"
            )
        )
    }
}
