package com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity

data class ChatUiState(
    val threads: List<ChatThreadView> = emptyList(),
    val currentThread: ChatThreadView? = null,
    val currentMessages: List<Message> = emptyList(),
    val currentThreadId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
