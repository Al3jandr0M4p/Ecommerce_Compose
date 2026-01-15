package com.clay.ecommerce_compose.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Wallet(
    val balance: Double
)

@Serializable
data class WalletTransactionInsertDTO(
    @SerialName("user_id")
    val userId: String,
    val amount: Double,
    val type: String,
    val reason: String? = null,
    @SerialName("reference_id")
    val referenceId: String? = null,
)


data class WalletTransaction(
    val id: String,
    val amount: Double,
    val type: String, // "credit" o "debit"
    val reason: String?,
    val createdAt: String,
    val orderId: String? = null
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

@Serializable
data class WalletTransactionDTO(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val amount: Double,
    val type: String, // "credit" | "debit"
    val reason: String? = null,
    @SerialName("reference_id")
    val referenceId: String? = null,
    @SerialName("created_at")
    val createdAt: String
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

    suspend fun getTransactions(): List<WalletTransaction> {
        val result = supabase.from("wallet_transactions")
            .select {
                filter { eq("user_id", userId()) }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<WalletTransactionDTO>()

        return result.map { dto ->
            WalletTransaction(
                id = dto.id,
                amount = dto.amount,
                type = dto.type,
                reason = dto.reason,
                createdAt = dto.createdAt,
                orderId = dto.referenceId
            )
        }
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

    suspend fun registerPayment(amount: Double, reason: String, type: String = "debit", orderId: String? = null) {
        val dto = WalletTransactionInsertDTO(
            userId = userId(),
            amount = amount,
            type = type,
            reason = reason,
            referenceId = orderId,
        )

        supabase.from("wallet_transactions").insert(dto)
    }


    suspend fun deductBalance(amount: Double, orderId: String? = null) {
        supabase.from("wallets").update(mapOf("balance" to "balance - $amount")) {
            filter { eq("user_id", userId()) }
        }

        supabase.from("wallet_transactions").insert(
            mapOf(
                "user_id" to userId(),
                "amount" to amount,
                "type" to "debit",
                "reason" to "Pago de compra",
                "reference_id" to orderId
            )
        )
    }
}
