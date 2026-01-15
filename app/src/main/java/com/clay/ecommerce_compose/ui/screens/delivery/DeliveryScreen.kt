package com.clay.ecommerce_compose.ui.screens.delivery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.utils.helpers.isLocationEnabled
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryHomeScreen(navController: NavHostController) {

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapViewportState = rememberMapViewportState()

    var userPoint by remember { mutableStateOf<Point?>(null) }
    var isTracking by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    var showEnableLocationDialog by remember { mutableStateOf(false) }

    // Callback para actualizaciones de ubicación en tiempo real
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    val point = Point.fromLngLat(location.longitude, location.latitude)
                    userPoint = point

                    // Actualizar cámara solo si es la primera vez
                    if (!isTracking) {
                        mapViewportState.setCameraOptions {
                            center(point)
                            zoom(17.0)
                        }
                        isTracking = true
                    }
                }
            }
        }
    }

    // Configurar actualizaciones de ubicación en tiempo real
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return@LaunchedEffect

        if (!isLocationEnabled(context)) {
            showEnableLocationDialog = true
            return@LaunchedEffect
        }

        try {
            // Obtener ubicación inicial
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                val point = Point.fromLngLat(it.longitude, it.latitude)
                userPoint = point

                mapViewportState.setCameraOptions {
                    center(point)
                    zoom(17.0)
                }
            }

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000L
            ).apply {
                setMinUpdateIntervalMillis(2000L)
                setWaitForAccurateLocation(true)
            }.build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Detener actualizaciones cuando se sale de la pantalla
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME) {

                if (isLocationEnabled(context)) {
                    showEnableLocationDialog = false
                    isTracking = false

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val point = Point.fromLngLat(it.longitude, it.latitude)
                            userPoint = point

                            mapViewportState.setCameraOptions {
                                center(point)
                                zoom(17.0)
                            }
                        }
                    }
                } else {
                    showEnableLocationDialog = true
                }

            }

        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val systemUIController = rememberSystemUiController()

    SideEffect {
        systemUIController.setStatusBarColor(
            color = Color.Transparent, darkIcons = true
        )
    }

    BottomSheetScaffold(
        sheetContent = {
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
//                    .height(height = 400.dp)
                    .padding(all = 16.dp),
                contentAlignment = Alignment.Companion.Center
            ) {
                Text(
                    text = "No tienes ordenes pendientes",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Companion.Center
                )
            }
        },
        sheetPeekHeight = 130.dp,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            MapboxMap(
                modifier = Modifier.Companion.fillMaxSize(), mapViewportState = mapViewportState
            ) {
                MapEffect(userPoint) { mapView ->

                    val annotationApi = mapView.annotations
                    val pointManager = annotationApi.createPointAnnotationManager()

                    pointManager.deleteAll()

                    userPoint?.let { point ->
                        val options = PointAnnotationOptions().withPoint(point)
                            .withIconImage("delivery_marker").withIconSize(1.1)
                            .withIconAnchor(IconAnchor.BOTTOM)

                        pointManager.create(options)
                    }
                }
            }

            if (showEnableLocationDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = {
                        Text(text = "Ubicacion desactivada")
                    },
                    text = {
                        Text(text = "Para continuar con el delivery necesitas activar la ubicacion del dispositivo")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showEnableLocationDialog = false
                                context.startActivity(
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                )
                            },
                        ) {
                            Text(text = "Activar ubicacion")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showEnableLocationDialog = false
                            }
                        ) {
                            Text(text = "Cancelar")
                        }
                    }
                )
            }
        }
    }
}
