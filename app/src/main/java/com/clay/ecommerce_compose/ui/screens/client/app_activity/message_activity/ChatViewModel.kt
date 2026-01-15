package com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.ChatRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ThreadType {
    @SerialName("user_business")
    USER_BUSINESS,

    @SerialName("user_delivery")
    USER_DELIVERY,

    @SerialName("delivery_business")
    DELIVERY_BUSINESS
}

@Serializable
data class ChatThread(
    val id: String,
    @SerialName("thread_type")
    val threadType: ThreadType,
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("business_id")
    val businessId: Int? = null,
    @SerialName("delivery_id")
    val deliveryId: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class Message(
    val id: String,
    @SerialName("thread_id")
    val threadId: String,
    @SerialName("sender_id")
    val senderId: String,
    val content: String,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class UserInfo(
    val id: String,
    val email: String
)

@Serializable
data class BusinessInfo(
    val id: Int,
    val name: String,
    @SerialName("logo_url")
    val logoUrl: String?
)

@Serializable
data class LastMessage(
    val id: String,
    val content: String,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("is_read")
    val isRead: Boolean
)

@Serializable
data class ChatThreadView(
    val id: String,
    @SerialName("thread_type")
    val threadType: ThreadType,
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("business_id")
    val businessId: Int? = null,
    @SerialName("delivery_id")
    val deliveryId: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("user_info")
    val userInfo: UserInfo? = null,
    @SerialName("business_info")
    val businessInfo: BusinessInfo? = null,
    @SerialName("delivery_info")
    val deliveryInfo: UserInfo? = null,
    @SerialName("last_message")
    val lastMessage: LastMessage? = null,
    @SerialName("unread_count")
    val unreadCount: Int = 0
)

@Serializable
data class CreateThreadRequest(
    @SerialName("thread_type")
    val threadType: ThreadType,
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("business_id")
    val businessId: Int? = null,
    @SerialName("delivery_id")
    val deliveryId: Int? = null
)

@Serializable
data class SendMessageRequest(
    @SerialName("thread_id")
    val threadId: String,
    @SerialName("sender_id")
    val senderId: String,
    val content: String
)

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var messageObserverJob: Job? = null

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.LoadThreads -> loadThreads()
            is ChatIntent.LoadMessages -> loadMessages(intent.threadId)
            is ChatIntent.SendMessage -> sendMessage(
                threadId = intent.threadId,
                senderId = intent.senderId,
                content = intent.content
            )
            is ChatIntent.MarkThreadAsRead -> markThreadAsRead(
                threadId = intent.threadId,
                userId = intent.userId
            )
            is ChatIntent.CreateUserBusinessThread -> createUserBusinessThread(
                userId = intent.userId,
                businessId = intent.businessId
            )
            is ChatIntent.CreateUserDeliveryThread -> createUserDeliveryThread(
                userId = intent.userId,
                deliveryId = intent.deliveryId
            )
            is ChatIntent.SelectThread -> {
                Log.d("ChatViewModel", "SelectThread: ${intent.thread.id}")
                _uiState.update {
                    it.copy(
                        currentThread = intent.thread,
                        currentThreadId = intent.thread.id,
                        currentMessages = emptyList(),
                        isLoading = true,
                        error = null
                    )
                }
                loadMessages(intent.thread.id)
            }
            is ChatIntent.SubscribeToThread -> subscribeToThread(intent.threadId)
            is ChatIntent.UnSubscribeFromThread -> unsubscribeFromThread()
        }
    }

    private fun loadThreads() {
        viewModelScope.launch {
            Log.d("ChatViewModel", "loadThreads: iniciando carga")
            _uiState.update { it.copy(isLoading = true, error = null) }

            chatRepository.getUserThreads()
                .onSuccess { threads ->
                    Log.d("ChatViewModel", "loadThreads: ${threads.size} threads cargados")
                    _uiState.update {
                        it.copy(
                            threads = threads,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    Log.e("ChatViewModel", "loadThreads: error", exception)
                    _uiState.update {
                        it.copy(
                            error = exception.message ?: "Error al cargar conversaciones",
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun loadMessages(threadId: String) {
        viewModelScope.launch {
            Log.d("ChatViewModel", "loadMessages: threadId=$threadId")
            _uiState.update { it.copy(isLoading = true, error = null, currentThreadId = threadId) }

            chatRepository.getThreadMessages(threadId)
                .onSuccess { messages ->
                    Log.d("ChatViewModel", "loadMessages: ${messages.size} mensajes cargados")
                    _uiState.update {
                        it.copy(
                            currentMessages = messages,
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    Log.e("ChatViewModel", "loadMessages: error", e)
                    _uiState.update {
                        it.copy(
                            error = e.message ?: "Error al cargar mensajes",
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun sendMessage(threadId: String, senderId: String, content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            Log.d("ChatViewModel", "sendMessage: threadId=$threadId, content=$content")
            chatRepository.sendMessage(threadId, senderId, content)
                .onSuccess { newMessage ->
                    Log.d("ChatViewModel", "sendMessage: mensaje enviado ${newMessage.id}")
                    // El mensaje se agregará automáticamente por el observer en tiempo real
                }
                .onFailure { error ->
                    Log.e("ChatViewModel", "sendMessage: error", error)
                    _uiState.update {
                        it.copy(error = error.message ?: "Error al enviar mensaje")
                    }
                }
        }
    }

    private fun markThreadAsRead(threadId: String, userId: String) {
        viewModelScope.launch {
            chatRepository.markThreadAsRead(threadId, userId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            threads = state.threads.map { thread ->
                                if (thread.id == threadId) {
                                    thread.copy(unreadCount = 0)
                                } else {
                                    thread
                                }
                            }
                        )
                    }
                }
        }
    }

    private fun createUserBusinessThread(userId: String, businessId: Int) {
        viewModelScope.launch {
            Log.d("ChatViewModel", "createUserBusinessThread: userId=$userId, businessId=$businessId")
            _uiState.update { it.copy(isLoading = true, error = null) }

            chatRepository.getOrCreateUserBusinessThread(userId, businessId)
                .onSuccess { thread ->
                    Log.d("ChatViewModel", "createUserBusinessThread: thread creado/encontrado ${thread.id}")

                    val threadView = convertThreadToView(thread, businessId)

                    // Cargar mensajes primero
                    val messages = chatRepository.getThreadMessages(thread.id).getOrNull() ?: emptyList()
                    Log.d("ChatViewModel", "createUserBusinessThread: ${messages.size} mensajes cargados")

                    // Actualizar estado UNA SOLA VEZ con todo listo
                    _uiState.update {
                        it.copy(
                            currentThreadId = thread.id,
                            currentThread = threadView,
                            currentMessages = messages,
                            isLoading = false, // IMPORTANTE: marcar como no cargando
                        )
                    }

                    // Recargar lista de threads en segundo plano
                    loadThreads()
                }
                .onFailure { error ->
                    Log.e("ChatViewModel", "createUserBusinessThread: error", error)
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Error al crear conversación",
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun createUserDeliveryThread(userId: String, deliveryId: Int) {
        viewModelScope.launch {
            Log.d("ChatViewModel", "createUserDeliveryThread: userId=$userId, deliveryId=$deliveryId")
            _uiState.update { it.copy(isLoading = true, error = null) }

            chatRepository.getOrCreateUserDeliveryThread(userId, deliveryId)
                .onSuccess { thread ->
                    Log.d("ChatViewModel", "createUserDeliveryThread: thread creado/encontrado ${thread.id}")

                    val threadView = convertThreadToView(thread, null, deliveryId)

                    // Cargar mensajes primero
                    val messages = chatRepository.getThreadMessages(thread.id).getOrNull() ?: emptyList()
                    Log.d("ChatViewModel", "createUserDeliveryThread: ${messages.size} mensajes cargados")

                    // Actualizar estado UNA SOLA VEZ con todo listo
                    _uiState.update {
                        it.copy(
                            currentThreadId = thread.id,
                            currentThread = threadView,
                            currentMessages = messages,
                            isLoading = false, // IMPORTANTE: marcar como no cargando
                        )
                    }

                    // Recargar lista de threads en segundo plano
                    loadThreads()
                }
                .onFailure { error ->
                    Log.e("ChatViewModel", "createUserDeliveryThread: error", error)
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Error al crear conversación",
                            isLoading = false
                        )
                    }
                }
        }
    }

    private suspend fun convertThreadToView(
        thread: ChatThread,
        businessId: Int? = null,
        deliveryId: Int? = null
    ): ChatThreadView {
        val businessInfo = if (businessId != null) {
            chatRepository.getBusinessInfo(businessId).getOrNull()
        } else null

        return ChatThreadView(
            id = thread.id,
            threadType = thread.threadType,
            userId = thread.userId,
            businessId = thread.businessId,
            deliveryId = thread.deliveryId,
            createdAt = thread.createdAt,
            updatedAt = thread.updatedAt,
            businessInfo = businessInfo,
            deliveryInfo = null,
            userInfo = null,
            lastMessage = null,
            unreadCount = 0
        )
    }

    private fun subscribeToThread(threadId: String) {
        // Cancelar suscripción anterior
        messageObserverJob?.cancel()

        viewModelScope.launch {
            try {
                Log.d("ChatViewModel", "subscribeToThread: $threadId")

                chatRepository.subscribeToChannel("messages:$threadId")

                // Observar nuevos mensajes en tiempo real en un Job separado
                messageObserverJob = viewModelScope.launch {
                    chatRepository.observeThreadMessages(threadId)
                        .catch { e ->
                            Log.e("ChatViewModel", "Error observando mensajes", e)
                        }
                        .collect { newMessage ->
                            Log.d("ChatViewModel", "Nuevo mensaje recibido: ${newMessage.id}")
                            _uiState.update { state ->
                                // Solo agregar si no existe ya
                                if (state.currentMessages.none { it.id == newMessage.id }) {
                                    state.copy(
                                        currentMessages = state.currentMessages + newMessage
                                    )
                                } else {
                                    state
                                }
                            }
                        }
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error al suscribirse al thread", e)
            }
        }
    }

    private fun unsubscribeFromThread() {
        messageObserverJob?.cancel()
        messageObserverJob = null

        viewModelScope.launch {
            _uiState.value.currentThreadId?.let { threadId ->
                Log.d("ChatViewModel", "unsubscribeFromThread: $threadId")
                try {
                    chatRepository.unsubscribeFromChannel("messages:$threadId")
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Error al desuscribirse", e)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        unsubscribeFromThread()
    }
}