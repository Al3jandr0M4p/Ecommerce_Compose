package com.clay.ecommerce_compose.ui.components.client.header

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.ui.components.client.cart.ShoppingCart
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.utils.helpers.updateLocationTextFromAddress
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HeaderUserHome(navController: NavHostController, cartViewModel: CartViewModel) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var locationText by remember { mutableStateOf(value = "Tu ubicacion") }
    var showLocationBanner by remember { mutableStateOf(value = false) }
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        while (true) {
            val hasPermission = permissionState.status.isGranted
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (hasPermission) {
                if (isGpsEnabled) {
                    showLocationBanner = false

                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                val geocoder = Geocoder(context)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    geocoder.getFromLocation(
                                        location.latitude,
                                        location.longitude,
                                        1
                                    ) { addresses ->
                                        locationText = if (addresses.isNotEmpty()) {
                                            updateLocationTextFromAddress(addresses[0])
                                        } else {
                                            "Ubicacion no disponible"
                                        }
                                    }
                                } else {
                                    try {
                                        @Suppress("DEPRECATION")
                                        val addresses = geocoder.getFromLocation(
                                            location.latitude,
                                            location.longitude,
                                            1
                                        )
                                        locationText = if (!addresses.isNullOrEmpty()) {
                                            updateLocationTextFromAddress(addresses[0])
                                        } else {
                                            "Ubicacion no disponible"
                                        }
                                    } catch (e: Exception) {
                                        locationText = "Ubicacion no disponible"
                                    }
                                }
                            } else {
                                locationText = "Ubicacion no disponible"
                            }
                        }
                    }

                } else {
                    locationText = "GPS desactivado"
                    showLocationBanner = true
                }
            } else {
                locationText = "Permisos de ubicacion necesarios"
                showLocationBanner = true
            }

            delay(1500)
        }
    }

    if (showLocationBanner) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .background(color = Color(color = 0xFFFFC107))
                .clickable {
                    if (!permissionState.status.isGranted) {
                        permissionState.launchPermissionRequest()
                    } else {
                        val intent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                        context.startActivity(intent)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = WindowInsets.statusBars.asPaddingValues())
                    .padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )

                Text(
                    text = "La opcion de compartir ubicacion esta\ndesactivada. Haz clic aqui para activarla",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Left,
                    maxLines = 2,
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(horizontal = 8.dp)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(size = 20.dp)
                )
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
    ) {
        Column {
            Text(
                text = "Entregar ahora",
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = locationText,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 16.sp,
                maxLines = 3,
                modifier = Modifier
                    .width(width = 190.dp)
                    .clickable {
                        if (!permissionState.status.isGranted) {
                            permissionState.launchPermissionRequest()
                        } else {
                            Toast.makeText(
                                context,
                                "Tu ubicación ya esta activa",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            )
        }

        Row {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(size = 28.dp)
                )
            }
            ShoppingCart(navController = navController, cartViewModel = cartViewModel)
        }
    }
}
