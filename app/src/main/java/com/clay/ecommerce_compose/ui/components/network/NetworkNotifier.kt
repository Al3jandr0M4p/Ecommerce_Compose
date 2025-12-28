package com.clay.ecommerce_compose.ui.components.network

import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.clay.ecommerce_compose.utils.helpers.ConnectivityReceiver


@Composable
fun rememberNetworkStatus(): Boolean {
    val context = LocalContext.current
    var networkAvailable by remember { mutableStateOf(ConnectivityReceiver.isNetworkAvailable(context)) }

    DisposableEffect(Unit) {
        val receiver = ConnectivityReceiver { isAvailable ->
            if (!isAvailable) {
                ConnectivityReceiver.showNoInternetNotification(context)
            }
            networkAvailable = isAvailable
        }

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(receiver, intentFilter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    return networkAvailable
}
