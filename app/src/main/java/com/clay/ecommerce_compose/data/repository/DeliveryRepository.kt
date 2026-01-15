package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.domain.model.Delivery
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class DeliveryRepository(private val supabase: SupabaseClient) {

    suspend fun getAllDeliveries(): List<Delivery> {
        return try {
            supabase.from("deliveries")
                .select()
                .decodeList<Delivery>()
        } catch (e: Exception) {
            Log.e("DeliveryRepository", "Error al obtener deliveries: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun createDelivery(
        customerName: String,
        deliveryPerson: String,
        phone: String
    ) {
        supabase.from("deliveries").insert(
            buildJsonObject {
                put("order_id", JsonPrimitive(generateOrderId()))
                put("customer_name", JsonPrimitive(customerName))
                put("delivery_person", JsonPrimitive(deliveryPerson))
                put("status", JsonPrimitive("PENDING"))
                put("address", JsonPrimitive("")) // Vacío por defecto
                put("phone", JsonPrimitive(phone))
                put("total", JsonPrimitive(0.0)) // 0 por defecto
            }
        )
    }

    suspend fun updateDelivery(
        id: String,
        customerName: String,
        deliveryPerson: String,
        phone: String
    ) {
        supabase.from("deliveries").update(
            buildJsonObject {
                put("customer_name", JsonPrimitive(customerName))
                put("delivery_person", JsonPrimitive(deliveryPerson))
                put("phone", JsonPrimitive(phone))
            }
        ) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteDelivery(id: String) {
        supabase.from("deliveries").delete {
            filter { eq("id", id) }
        }
    }

    private fun generateOrderId(): String {
        return "ORD-${System.currentTimeMillis()}"
    }
}