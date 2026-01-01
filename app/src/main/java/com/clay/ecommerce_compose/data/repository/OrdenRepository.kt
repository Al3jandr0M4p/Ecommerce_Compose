package com.clay.ecommerce_compose.data.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val payment_method: String,
    val coupon_code: String? = null
)

@Serializable
data class CreateOrderResponse(
    @SerialName("order_id")
    val orderId: String,
    val total: Double,
    val status: String
)

class OrderRepository(private val supabase: SupabaseClient, private val httpClient: HttpClient) {

    private fun accessToken(): String = supabase.auth.currentSessionOrNull()?.accessToken
        ?: throw IllegalStateException("Usuario no autenticado")

    suspend fun createOrder(
        paymentMethod: String,
        couponCode: String?
    ): CreateOrderResponse {
        val token = accessToken()
        Log.d("ORDER_REPO", "AccessToken: $token")
        Log.d(
            "ORDER_REPO",
            "Creando orden con paymentMethod=$paymentMethod, couponCode=$couponCode"
        )

        return httpClient.post("https://6gszhspz-3000.use2.devtunnels.ms/api/orders/") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            setBody(
                CreateOrderRequest(
                    payment_method = paymentMethod,
                    coupon_code = couponCode,
                )
            )
        }.body()

    }

}
