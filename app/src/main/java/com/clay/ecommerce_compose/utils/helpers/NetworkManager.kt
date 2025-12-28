package com.clay.ecommerce_compose.utils.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.app.NotificationCompat
import com.clay.ecommerce_compose.R

class ConnectivityReceiver(private val onNetworkChange: (Boolean) -> Unit) : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "network_status_channel"

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        fun showNoInternetNotification(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "Estado de la Red",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notificaciones sobre el estado de la conexion a internet"
                }
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_wifi_off)
                .setContentTitle("Sin conexion a Internet")
                .setContentText("Por favor, activa el Wi-Fi o los datos moviles para continuar.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(1, notification)
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.net.conn.CONNECTIVITY_CHANGE") {
            onNetworkChange(isNetworkAvailable(context))
        }
    }
}
