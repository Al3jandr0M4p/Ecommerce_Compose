package com.clay.ecommerce_compose.notifications

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.clay.ecommerce_compose.activity.AppHandleMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.clay.ecommerce_compose.R

class ClickMarquetFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(message)
    }

    private fun showNotification(message: RemoteMessage) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, AppHandleMessaging.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.ic_apple)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }
}
