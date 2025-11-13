package com.clay.ecommerce_compose.ui.components.auth.business


import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.clay.ecommerce_compose.BuildConfig
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.plugins.annotation.Symbol
import org.maplibre.android.plugins.annotation.SymbolManager
import org.maplibre.android.plugins.annotation.SymbolOptions

@Composable
fun LocationPicker(
    latitude: Double?,
    longitude: Double?,
    onLocationSelected: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    remember {
        try {
            MapLibre.getInstance(context)
        } catch (e: Exception) {
            Log.e("LocalPicker", "Error al iniciarlizar ${e.message}")
        }
    }

    val mapView = remember {
        MapView(context).apply {
            id = View.generateViewId()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) = mapView.onStart()
            override fun onResume(owner: LifecycleOwner) = mapView.onResume()
            override fun onPause(owner: LifecycleOwner) = mapView.onPause()
            override fun onStop(owner: LifecycleOwner) = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner) = mapView.onDestroy()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        AndroidView(
            factory = { ctx ->
                mapView.apply {
                    getMapAsync { map ->
                        map.setStyle(
                            Style.Builder().fromUri("https://api.maptiler.com/maps/streets/style.json?key=${BuildConfig.MAP_KEY}")
                        ) { style ->
                            val drawable = AppCompatResources.getDrawable(
                                ctx,
                                android.R.drawable.ic_menu_mylocation
                            )
                            drawable?.let {
                                style.addImage("marker-icon", it.toBitmap())
                            }

                            val initialLat = latitude ?: 18.4861
                            val initialLng = longitude ?: -69.9312

                            val cameraPosition = CameraPosition.Builder()
                                .target(LatLng(initialLat, initialLng))
                                .zoom(13.0)
                                .build()
                            map.cameraPosition = cameraPosition

                            val symbolManager = SymbolManager(mapView, map, style).apply {
                                iconAllowOverlap = true
                                iconIgnorePlacement = true
                            }

                            var currentSymbol: Symbol? = null

                            map.addOnMapClickListener { point ->
                                val lat = point.latitude
                                val lng = point.longitude

                                currentSymbol?.let { symbolManager.delete(it) }

                                currentSymbol = symbolManager.create(
                                    SymbolOptions()
                                        .withLatLng(LatLng(lat, lng))
                                        .withIconImage("marker-icon")
                                        .withIconSize(1.3f)
                                )

                                onLocationSelected(lat, lng)
                                true
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
