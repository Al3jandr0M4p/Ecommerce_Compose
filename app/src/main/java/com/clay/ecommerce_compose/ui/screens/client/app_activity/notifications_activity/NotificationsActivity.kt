package com.clay.ecommerce_compose.ui.screens.client.app_activity.notifications_activity

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.activity.MainViewModel
import com.clay.ecommerce_compose.ui.components.client.app_activity.EmptyNotifications
import com.clay.ecommerce_compose.ui.components.client.app_activity.NotificationItems
import com.clay.ecommerce_compose.ui.components.client.header.NotificationType
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ChatIntent
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.ChatViewModel
import com.clay.ecommerce_compose.ui.screens.client.app_activity.message_activity.MessageActivity
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

enum class ActivityTab {
    NOTIFICATIONS,
    MESSAGES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsActivity(
    businessId: Int?,
    navController: NavHostController,
    cartViewModel: CartViewModel,
    chatViewModel: ChatViewModel,
    mainViewModel: MainViewModel
) {
    val notifications by cartViewModel.notifications.collectAsState()
    var selectedTab by remember { mutableStateOf(value = ActivityTab.NOTIFICATIONS) }

    LaunchedEffect(notifications.size) {
        Log.d("NotificationsActivity", "Notifications count: ${notifications.size}")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Actividad",
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }, navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(route = "userHome") },
                        modifier = Modifier.Companion.size(size = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.white)
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white))
                .padding(paddingValues = innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 8.dp)
            ) {
                Box(modifier = Modifier.Companion.padding(end = 16.dp)) {
                    Text(
                        text = "Notificaciones",
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500),
                        color = if (selectedTab == ActivityTab.NOTIFICATIONS) colorResource(id = R.color.black)
                        else colorResource(id = R.color.coolGreyLight),
                        modifier = Modifier.Companion
                            .padding(end = 10.dp)
                            .clickable {
                                selectedTab = ActivityTab.NOTIFICATIONS
                            }
                    )
                    if (notifications.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(size = 12.dp)
                                .background(Color.Red, CircleShape)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
                Text(
                    text = "Mensajes",
                    fontSize = 22.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500),
                    color = if (selectedTab == ActivityTab.MESSAGES) colorResource(id = R.color.black)
                    else colorResource(id = R.color.coolGreyLight),
                    modifier = Modifier.Companion.clickable {
                        selectedTab = ActivityTab.MESSAGES
                    }
                )
            }

            when (selectedTab) {
                ActivityTab.NOTIFICATIONS -> {
                    if (notifications.isEmpty()) {
                        EmptyNotifications(modifier = Modifier.weight(weight = 1f))
                    } else {
                        LazyColumn(modifier = Modifier.Companion.fillMaxSize()) {
                            items(
                                items = notifications,
                                key = { it.id }
                            ) { n ->
                                NotificationItems(
                                    notification = n,
                                    onClick = {
                                        when (n.type) {
                                            NotificationType.STOCK_LOW -> {
                                                val productId = n.id
                                                    .removePrefix("stock-")
                                                    .substringBefore("-")
                                                navController.navigate("product/$productId")
                                            }

                                            NotificationType.PROMO ->
                                                navController.navigate("promo/${n.id}")

                                            NotificationType.ORDER_STATUS ->
                                                navController.navigate("order-status")
                                        }

                                        cartViewModel.removeNotification(n.id)
                                    }
                                )
                            }
                        }
                    }
                }

                ActivityTab.MESSAGES -> {
                    MessageActivity(
                        chatViewModel = chatViewModel,
                        mainViewModel = mainViewModel,
                        businessId = businessId,
                        onThreadClick = { thread ->
                            chatViewModel.handleIntent(intent = ChatIntent.SelectThread(thread = thread))
                            navController.navigate(route = "chat")
                        }
                    )
                }
            }
        }
    }
}
