package com.clay.ecommerce_compose.data.repository

import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ChatThread
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ChatThreadView
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.CreateThreadRequest
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.Message
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.SendMessageRequest
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ThreadType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.decodeIfNotEmptyOrDefault
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
        // Primero intenta obtener el thread existente
        val existing = supabase.from("chat_threads")
            .select {
                filter {
                    eq("user_id", userId)
                    eq("business_id", businessId)
                    eq("thread_type", ThreadType.USER_BUSINESS.name.lowercase())
                }
            }
            .decodeSingleOrNull<ChatThread>()

        if (existing != null) {
            return@runCatching existing
        }

        // Si no existe, créalo
        val newThread = CreateThreadRequest(
            threadType = ThreadType.USER_BUSINESS,
            userId = userId,
            businessId = businessId
        )

        supabase.from("chat_threads")
            .insert(newThread)
            .decodeSingle<ChatThread>()
    }

    suspend fun getOrCreateUserDeliveryThread(
        userId: String,
        deliveryId: Int
    ): Result<ChatThread> = runCatching {
        val existing = supabase.from("chat_threads")
            .select {
                filter {
                    eq("user_id", userId)
                    eq("delivery_id", deliveryId)
                    eq("thread_type", ThreadType.USER_DELIVERY.name.lowercase())
                }
            }
            .decodeSingleOrNull<ChatThread>()

        if (existing != null) {
            return@runCatching existing
        }

        val newThread = CreateThreadRequest(
            threadType = ThreadType.USER_DELIVERY,
            userId = userId,
            deliveryId = deliveryId
        )

        supabase.from("chat_threads")
            .insert(newThread)
            .decodeSingle<ChatThread>()
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
        supabase.from("chat_threads_view")
            .select()
            .decodeList<ChatThreadView>()
            .sortedByDescending { it.updatedAt }
    }


    suspend fun getThreadMessages(threadId: String): Result<List<Message>> = runCatching {
        supabase.from("messages")
            .select {
                filter {
                    eq("thread_id", threadId)
                }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<Message>()
    }

    suspend fun sendMessage(
        threadId: String,
        senderId: String,
        content: String
    ): Result<Message> = runCatching {
        val request = SendMessageRequest(
            threadId = threadId,
            senderId = senderId,
            content = content
        )

        supabase.from("messages")
            .insert(request)
            .decodeSingle<Message>()
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

    suspend fun markThreadAsRead(threadId: String, currentUserId: String): Result<Unit> = runCatching {
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

    @OptIn(SupabaseInternal::class)
    fun observeThreadMessages(threadId: String): Flow<Message> {
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
        supabase.realtime.channel(channelName).subscribe()
    }

    suspend fun unsubscribeFromChannel(channelName: String) {
        supabase.realtime.channel(channelName).unsubscribe()
    }

}
