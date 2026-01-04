package com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity

sealed class ChatIntent {
    data class LoadThreads(val userId: String) : ChatIntent()
    data class LoadMessages(val threadId: String) : ChatIntent()
    data class SelectThread(val thread: ChatThreadView) : ChatIntent()

    data class SendMessage(val threadId: String, val senderId: String, val content: String) : ChatIntent()
    data class MarkThreadAsRead(val threadId: String, val userId: String) : ChatIntent()
    data class CreateUserBusinessThread(val userId: String, val businessId: Int) : ChatIntent()
    data class CreateUserDeliveryThread(val userId: String, val deliveryId: Int) : ChatIntent()
    data class SubscribeToThread(val threadId: String) : ChatIntent()

    data object UnSubscribeFromThread : ChatIntent()
}
