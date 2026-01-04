package com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.activity.MainViewModel
import com.clay.ecommerce_compose.activity.SplashState
import com.clay.ecommerce_compose.utils.helpers.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageActivity(
    chatViewModel: ChatViewModel,
    onThreadClick: (ChatThreadView) -> Unit,
    mainViewModel: MainViewModel,
    businessId: Int?,
) {
    val uiState by chatViewModel.uiState.collectAsState()

    val splashState by mainViewModel.splashState.collectAsState()
    val currentUserId = (splashState as? SplashState.Authenticated)
        ?.session
        ?.id
        ?: return

    val autoNavigated = remember { mutableStateOf(false) }

    LaunchedEffect(businessId) {
        if (businessId != null) {
            chatViewModel.handleIntent(
                intent = ChatIntent.CreateUserBusinessThread(
                    userId = currentUserId,
                    businessId = businessId
                )
            )
        } else {
            chatViewModel.handleIntent(intent = ChatIntent.LoadThreads(currentUserId))
        }
    }

    LaunchedEffect(uiState.currentThreadId) {
        if (
            businessId != null &&
            uiState.currentThreadId != null &&
            !autoNavigated.value
        ) {
            autoNavigated.value = true
            onThreadClick(uiState.currentThread!!)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.threads.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.threads.isEmpty() -> {
                Text(
                    text = "No tienes conversaciones",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items = uiState.threads) { thread ->
                        ThreadItem(
                            thread = thread,
                            onClick = { onThreadClick(thread) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ThreadItem(
    thread: ChatThreadView,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            when {
                thread.businessInfo != null && thread.businessInfo.logoUrl != null -> {
                    AsyncImage(
                        model = thread.businessInfo.logoUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                thread.businessInfo != null -> {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                else -> {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (thread.threadType) {
                        ThreadType.USER_BUSINESS -> thread.businessInfo?.name ?: "Negocio"
                        ThreadType.USER_DELIVERY -> thread.deliveryInfo?.email ?: "Repartidor"
                        ThreadType.DELIVERY_BUSINESS -> thread.businessInfo?.name ?: "Negocio"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                thread.lastMessage?.let { lastMsg ->
                    Text(
                        text = formatTime(lastMsg.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = thread.lastMessage?.content ?: "Sin mensajes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (thread.unreadCount > 0) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = if (thread.unreadCount > 0) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (thread.unreadCount > 0) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = if (thread.unreadCount > 9) "9+" else thread.unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
