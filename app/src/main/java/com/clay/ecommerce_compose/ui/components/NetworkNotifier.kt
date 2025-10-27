package com.clay.ecommerce_compose.ui.components

import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.clay.ecommerce_compose.utils.ConnectivityReceiver
import com.clay.ecommerce_compose.utils.isNetworkAvailable
import com.clay.ecommerce_compose.utils.showNoInternetNotification

@Composable
fun rememberNetworkStatus(): Boolean {
    val context = LocalContext.current
    var NetworkAvailable by remember { mutableStateOf(isNetworkAvailable(context)) }

    DisposableEffect(Unit) {
        val receiver = ConnectivityReceiver { isAvailable ->
            if (!isAvailable) {
                showNoInternetNotification(context)
            }
            NetworkAvailable = isAvailable
        }

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(receiver, intentFilter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    return NetworkAvailable
}
