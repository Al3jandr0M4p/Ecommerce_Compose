package com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity

data class ChatMessage(
    val id: String,
    val threadId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long
)
