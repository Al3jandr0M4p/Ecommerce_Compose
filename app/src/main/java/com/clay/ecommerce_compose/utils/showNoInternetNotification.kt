package com.clay.ecommerce_compose.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.clay.ecommerce_compose.R

fun showNoInternetNotification(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "network_status_channel"

    val channel =
        NotificationChannel(
            channelId,
            "Estado de la Red",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones sobre el estado de la conexion a internet"
        }
    notificationManager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_wifi_off)
        .setContentTitle("Sin conexion a Internet")
        .setContentText("Por favor, activa el Wi-Fi o los datos moviles para continuar.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()
    notificationManager.notify(1, notification)

}
