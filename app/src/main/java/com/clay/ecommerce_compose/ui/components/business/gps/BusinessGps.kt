package com.clay.ecommerce_compose.ui.components.business.gps

import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun BusinessLocationText(latitude: Double?, longitude: Double?) {
    val context = LocalContext.current
    var addressText by remember { mutableStateOf(value = "Cargando ubicacion...") }

    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            val geocoder = Geocoder(context)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        addressText = if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            val calle = address.thoroughfare ?: "Calle desconocida"
                            val ciudad = address.locality ?: "Ciudad desconocida"
                            "$calle, $ciudad"
                        } else {
                            "Ubicación no disponible"
                        }
                    }
                } else {
                    /** @Deprecated */
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    addressText = if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val calle = address.thoroughfare ?: "Calle desconocida"
                        val ciudad = address.locality ?: "Ciudad desconocida"
                        "$calle, $ciudad"
                    } else {
                        "Ubicación no disponible"
                    }
                }
            } catch (e: Exception) {
                Log.e("BusinessLocationText", "Error al obtener dirección: ${e.message}")
                addressText = "Error obteniendo direccion"
            }
        } else {
            addressText = "Ubicacion no definida"
        }
    }

    Text(
        text = addressText, style = MaterialTheme.typography.bodySmall, color = Color.Gray
    )
}