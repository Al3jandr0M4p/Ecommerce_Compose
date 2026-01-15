package com.clay.ecommerce_compose.data.repository

import android.util.Log
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.BusinessInfo
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ChatThread
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ChatThreadView
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.CreateThreadRequest
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.Message
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.SendMessageRequest
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ThreadType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ChatRepository(private val supabase: SupabaseClient) {

    suspend fun getOrCreateUserBusinessThread(
        userId: String,
        businessId: Int
    ): Result<ChatThread> = runCatching {
        Log.d(
            "ChatRepository",
            "getOrCreateUserBusinessThread: userId=$userId, businessId=$businessId"
        )

        // Primero intenta obtener el thread existente
        val existing = try {
            supabase.from("chat_threads")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("business_id", businessId)
                        eq("thread_type", "user_business")
                    }
                }
                .decodeSingleOrNull<ChatThread>()
        } catch (e: Exception) {
            Log.w("ChatRepository", "Error buscando thread existente", e)
            null
        }

        if (existing != null) {
            Log.d("ChatRepository", "Thread existente encontrado: ${existing.id}")
            return@runCatching existing
        }

        // Si no existe, créalo
        Log.d("ChatRepository", "Creando nuevo thread")
        val newThread = CreateThreadRequest(
            threadType = ThreadType.USER_BUSINESS,
            userId = userId,
            businessId = businessId
        )

        val created = supabase.from("chat_threads")
            .insert(newThread) {
                select()
            }
            .decodeSingle<ChatThread>()

        Log.d("ChatRepository", "Thread creado exitosamente: ${created.id}")
        created
    }

    suspend fun getOrCreateUserDeliveryThread(
        userId: String,
        deliveryId: Int
    ): Result<ChatThread> = runCatching {
        Log.d(
            "ChatRepository",
            "getOrCreateUserDeliveryThread: userId=$userId, deliveryId=$deliveryId"
        )

        val existing = try {
            supabase.from("chat_threads")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("delivery_id", deliveryId)
                        eq("thread_type", ThreadType.USER_DELIVERY.name.lowercase())
                    }
                }
                .decodeSingleOrNull<ChatThread>()
        } catch (e: Exception) {
            Log.w("ChatRepository", "Error buscando thread existente", e)
            null
        }

        if (existing != null) {
            Log.d("ChatRepository", "Thread existente encontrado: ${existing.id}")
            return@runCatching existing
        }

        Log.d("ChatRepository", "Creando nuevo thread")
        val newThread = CreateThreadRequest(
            threadType = ThreadType.USER_DELIVERY,
            userId = userId,
            deliveryId = deliveryId
        )

        val created = supabase.from("chat_threads")
            .insert(newThread) {
                select()
            }
            .decodeSingle<ChatThread>()

        Log.d("ChatRepository", "Thread creado exitosamente: ${created.id}")
        created
    }

    suspend fun getOrCreateDeliveryBusinessThread(
        deliveryId: Int,
        businessId: Int
    ): Result<ChatThread> = runCatching {
        val existing = supabase.from("chat_threads")
            .select {
                filter {
                    eq("delivery_id", deliveryId)
                    eq("business_id", businessId)
                    eq("thread_type", ThreadType.DELIVERY_BUSINESS.name.lowercase())
                }
            }
            .decodeSingleOrNull<ChatThread>()

        if (existing != null) {
            return@runCatching existing
        }

        val newThread = CreateThreadRequest(
            threadType = ThreadType.DELIVERY_BUSINESS,
            deliveryId = deliveryId,
            businessId = businessId
        )

        supabase.from("chat_threads")
            .insert(newThread)
            .decodeSingle<ChatThread>()
    }


    suspend fun getUserThreads(): Result<List<ChatThreadView>> = runCatching {
        Log.d("ChatRepository", "getUserThreads: obteniendo threads")

        val threads = supabase.from("chat_threads_view")
            .select()
            .decodeList<ChatThreadView>()
            .sortedByDescending { it.updatedAt }

        Log.d("ChatRepository", "getUserThreads: ${threads.size} threads encontrados")
        threads
    }


    suspend fun getThreadMessages(threadId: String): Result<List<Message>> = runCatching {
        Log.d("ChatRepository", "getThreadMessages: threadId=$threadId")

        val messages = supabase.from("messages")
            .select {
                filter {
                    eq("thread_id", threadId)
                }
                order("created_at", Order.ASCENDING)
            }
            .decodeList<Message>()

        Log.d("ChatRepository", "getThreadMessages: ${messages.size} mensajes encontrados")
        messages
    }

    suspend fun sendMessage(
        threadId: String,
        senderId: String,
        content: String
    ): Result<Message> = runCatching {
        Log.d("ChatRepository", "sendMessage: threadId=$threadId, senderId=$senderId")

        val request = SendMessageRequest(
            threadId = threadId,
            senderId = senderId,
            content = content
        )

        val message = supabase.from("messages")
            .insert(request) {
                select()
            }
            .decodeSingle<Message>()

        Log.d("ChatRepository", "sendMessage: mensaje enviado ${message.id}")
        message
    }

    suspend fun markMessageAsRead(messageId: String): Result<Unit> = runCatching {
        supabase.from("messages")
            .update({
                set("is_read", true)
            }) {
                filter {
                    eq("id", messageId)
                }
            }
    }

    suspend fun markThreadAsRead(threadId: String, currentUserId: String): Result<Unit> =
        runCatching {
            Log.d(
                "ChatRepository",
                "markThreadAsRead: threadId=$threadId, currentUserId=$currentUserId"
            )

            supabase.from("messages")
                .update({
                    set("is_read", true)
                }) {
                    filter {
                        eq("thread_id", threadId)
                        eq("is_read", false)
                        neq("sender_id", currentUserId)
                    }
                }
        }

    suspend fun getBusinessInfo(businessId: Int): Result<BusinessInfo> = runCatching {
        Log.d("ChatRepository", "getBusinessInfo: businessId=$businessId")

        val business = supabase.from("businesses")
            .select {
                filter {
                    eq("id", businessId)
                }
            }
            .decodeSingle<BusinessInfo>()

        Log.d("ChatRepository", "getBusinessInfo: negocio encontrado: ${business.name}")
        business
    }

    @OptIn(SupabaseInternal::class)
    fun observeThreadMessages(threadId: String): Flow<Message> {
        Log.d("ChatRepository", "observeThreadMessages: configurando flujo para threadId=$threadId")

        val channel = supabase.realtime.channel("messages:$threadId")

        return channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "messages"
            filter("thread_id", FilterOperator.EQ, threadId)
        }.map { action ->
            Json.decodeFromJsonElement<Message>(action.record)
        }
    }

    fun observeUserThreads(): Flow<PostgresAction> {
        val channel = supabase.realtime.channel("user_threads")

        return channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "chat_threads"
        }
    }

    suspend fun subscribeToChannel(channelName: String) {
        try {
            Log.d("ChatRepository", "subscribeToChannel: $channelName")
            supabase.realtime.channel(channelName).subscribe()
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error al suscribirse al canal $channelName", e)
        }
    }

    suspend fun unsubscribeFromChannel(channelName: String) {
        try {
            Log.d("ChatRepository", "unsubscribeFromChannel: $channelName")
            supabase.realtime.channel(channelName).unsubscribe()
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error al desuscribirse del canal $channelName", e)
        }
    }

}
