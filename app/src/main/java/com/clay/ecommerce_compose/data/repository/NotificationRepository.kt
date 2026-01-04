package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.ui.components.client.header.NotificationItem
import com.clay.ecommerce_compose.ui.components.client.header.NotificationType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationInsert(
    @SerialName("user_id")
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    val read: Boolean = false
)

@Serializable
data class NotificationResponse(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    val read: Boolean,
    @SerialName("created_at")
    val createdAt: String? = null
)

class NotificationRepository(private val supabase: SupabaseClient) {

    private var realtimeChannel: RealtimeChannel? = null

    private fun userId() = supabase.auth.currentUserOrNull()?.id
        ?: throw IllegalStateException("User not authenticated")

    suspend fun getNotifications(): List<NotificationItem> {
        val usrId = userId()

        return supabase.from("notifications")
            .select {
                filter {
                    eq("user_id", usrId)
                    eq("read", false)
                }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<NotificationResponse>()
            .map { response ->
                NotificationItem(
                    id = response.id,
                    title = response.title,
                    message = response.message,
                    type = when (response.type) {
                        "ORDER_STATUS" -> NotificationType.ORDER_STATUS
                        "STOCK_LOW" -> NotificationType.STOCK_LOW
                        "PROMO" -> NotificationType.PROMO
                        else -> NotificationType.ORDER_STATUS
                    }
                )
            }
    }

    suspend fun insertNotification(notification: NotificationItem): String {
        try {
            val usrId = userId()

            val response = supabase.from("notifications")
                .insert(
                    value = NotificationInsert(
                        userId = usrId,
                        title = notification.title,
                        message = notification.message,
                        type = when (notification.type) {
                            NotificationType.ORDER_STATUS -> "ORDER_STATUS"
                            NotificationType.STOCK_LOW -> "STOCK_LOW"
                            NotificationType.PROMO -> "PROMO"
                        }
                    )
                ) {
                    select()
                }
                .decodeSingle<NotificationResponse>()

            Log.d("NotificationRepo", "Notification inserted: ${notification.title}")
            return response.id
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error inserting notification", e)
            throw e
        }
    }

    suspend fun listenNotifications(): Flow<NotificationItem> {
        val usrId = userId()

        realtimeChannel?.unsubscribe()

        val channel = supabase.channel("notifications-$usrId")
        realtimeChannel = channel

        val flow = channel.postgresChangeFlow<PostgresAction>(
            schema = "public"
        ) {
            table = "notifications"
        }.mapNotNull { action ->
            when (action) {
                is PostgresAction.Insert -> {
                    val record = action.record
                    if (record["user_id"]?.toString() == usrId) {
                        parseNotification(record)
                    } else {
                        null
                    }
                }

                else -> null
            }
        }

        channel.subscribe()
        Log.d("NotificationRepo", "Realtime channel subscribed: notifications-$usrId")

        return flow
    }

    suspend fun markAsRead(notificationId: String) {
        try {
            supabase.from("notifications").update(mapOf("read" to true)) {
                filter {
                    eq("id", notificationId)
                    eq("user_id", userId())
                }
            }

            Log.d("NotificationRepo", "Notification marked as read: $notificationId")
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error marking notification as read", e)
        }
    }

    suspend fun deleteNotification(notificationId: String) {
        try {
            supabase.from("notifications").delete {
                filter {
                    eq("id", notificationId)
                    eq("user_id", userId())
                }
            }

            Log.d("NotificationRepo", "Notification deleted: $notificationId")
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error deleting notification", e)
        }
    }

    private fun parseNotification(record: Map<String, Any?>): NotificationItem {
        return NotificationItem(
            id = record["id"]?.toString() ?: "",
            title = record["title"]?.toString() ?: "",
            message = record["message"]?.toString() ?: "",
            type = when (record["type"]?.toString()) {
                "ORDER_STATUS" -> NotificationType.ORDER_STATUS
                "STOCK_LOW" -> NotificationType.STOCK_LOW
                "PROMO" -> NotificationType.PROMO
                else -> NotificationType.ORDER_STATUS
            }
        )
    }

    suspend fun cleanup() {
        try {
            realtimeChannel?.unsubscribe()
            realtimeChannel = null
            Log.d("NotificationRepo", "realtime channel unsubscribed")
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error cleaning up", e)
        }
    }
}
