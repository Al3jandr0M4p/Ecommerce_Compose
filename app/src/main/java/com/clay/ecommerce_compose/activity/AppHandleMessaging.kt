package com.clay.ecommerce_compose.activity

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging


class AppHandleMessaging : Application() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification"
    }

    override fun onCreate() {
        super.onCreate()
        Firebase.messaging.token.addOnCompleteListener {
            if (!it.isSuccessful) {
                println("El token no fue generado")
                return@addOnCompleteListener
            }

            val token = it.result
            println("El token es> $token")
        }
        createNotificationsChannel()
    }

    private fun createNotificationsChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notificaciones De Supabase",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notificationes importantes"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
