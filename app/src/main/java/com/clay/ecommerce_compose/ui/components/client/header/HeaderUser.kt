package com.clay.ecommerce_compose.ui.components.client.header

import android.Manifest
import android.app.Activity
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.cart.ShoppingCart
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.utils.helpers.updateLocationTextFromAddress
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType, // STOCK_LOW, PROMO, ORDER_STATE, etc.
    val timestamp: Long = System.currentTimeMillis(),
    val orderId: String? = null
)

enum class NotificationType { STOCK_LOW, PROMO, ORDER_STATUS }


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HeaderUserHome(navController: NavHostController, cartViewModel: CartViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val windowSize = calculateWindowSizeClass(activity)
    val isTablet = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val notifications by cartViewModel.notifications.collectAsState()

    var locationText by remember { mutableStateOf(value = "Tu ubicacion") }
    var showLocationBanner by remember { mutableStateOf(value = false) }
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        while (true) {
            val hasPermission = permissionState.status.isGranted
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (hasPermission) {
                if (isGpsEnabled) {
                    showLocationBanner = false

                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                val geocoder = Geocoder(context)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    geocoder.getFromLocation(
                                        location.latitude, location.longitude, 1
                                    ) { addresses ->
                                        locationText = if (addresses.isNotEmpty()) {
                                            updateLocationTextFromAddress(addresses[0])
                                        } else {
                                            "Ubicacion no disponible"
                                        }
                                    }
                                } else {
                                    try {
                                        @Suppress("DEPRECATION") val addresses =
                                            geocoder.getFromLocation(
                                                location.latitude, location.longitude, 1
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
                .padding(vertical = if (isTablet) 18.dp else 14.dp)
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
                }, contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = WindowInsets.statusBars.asPaddingValues())
                    .padding(all = if (isTablet) 32.dp else 22.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )

                Text(
                    text = "Compartir la ubicacion esta desactivado",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = if (isTablet) 20.sp else 12.sp,
                    textAlign = TextAlign.Left,
                    maxLines = 2,
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(horizontal = if (isTablet) 14.dp else 8.dp)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(size = if (isTablet) 24.dp else 20.dp)
                )
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = if (isTablet) 22.dp else 20.dp)
    ) {
        Column {
            Text(
                text = "Entregar ahora",
                fontSize = if (isTablet) 16.sp else 12.sp,
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = locationText,
                style = MaterialTheme.typography.labelSmall,
                fontSize = if (isTablet) 22.sp else 16.sp,
                maxLines = 3,
                modifier = Modifier
                    .width(width = if (isTablet) 380.dp else 300.dp)
                    .clickable {
                        if (!permissionState.status.isGranted) {
                            permissionState.launchPermissionRequest()
                        } else {
                            Toast.makeText(
                                context, "Tu ubicación ya esta activa", Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }

        Row {
            Box {
                IconButton(onClick = { navController.navigate(route = "activity") }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(size = if (isTablet) 40.dp else 28.dp)
                    )
                }

                if (notifications.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .offset(
                                x = if (isTablet) (-5).dp else (-6).dp,
                                y = if (isTablet) 5.dp else 6.dp
                            )
                            .size(size = if (isTablet) 18.dp else 12.dp)
                            .background(color = colorResource(id = R.color.tintRed), shape = CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            ShoppingCart(
                navController = navController,
                cartViewModel = cartViewModel,
                isTablet = isTablet
            )
        }
    }
}
