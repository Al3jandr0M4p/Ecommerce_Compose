package com.clay.ecommerce_compose.ui.screens.delivery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

// Data classes
@Serializable
data class DeliveryOrder(
    val id: String,
    val user_id: String,
    val total: Double,
    val delivery_fee: Double,
    val status: String,
    val created_at: String,
    val customer_info: JsonObject,
    val businesses: List<JsonObject>,
    val item_count: Int
)

@Serializable
data class ActiveOrder(
    val id: String,
    val user_id: String,
    val delivery_id: String,
    val total: Double,
    val delivery_fee: Double,
    val status: String,
    val created_at: String,
    val delivery_accepted_at: String,
    val customer_info: JsonObject,
    val businesses: List<JsonObject>,
    val items: List<JsonObject>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryHomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapViewportState = rememberMapViewportState()
    val scope = rememberCoroutineScope()

    var userPoint by remember { mutableStateOf<Point?>(null) }
    var isTracking by remember { mutableStateOf(false) }
    var showEnableLocationDialog by remember { mutableStateOf(false) }
    var showComingSoonDialog by remember { mutableStateOf(false) }

    // Estados para órdenes
    var availableOrders by remember { mutableStateOf<List<DeliveryOrder>>(emptyList()) }
    var activeOrders by remember { mutableStateOf<List<ActiveOrder>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    val lifecycleOwner = LocalLifecycleOwner.current

    // TODO: Reemplazar con tu instancia de Supabase
    // val supabase = remember { createSupabaseClient() }

    suspend fun loadAvailableOrders() {
        isLoading = true
        try {
            // TODO: Implementar llamada a Supabase
            // val response = supabase.from("available_delivery_orders")
            //     .select()
            //     .decodeList<DeliveryOrder>()
            // availableOrders = response

            // Por ahora, datos de prueba
            availableOrders = listOf()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    suspend fun loadActiveOrders() {
        isLoading = true
        try {
            // TODO: Implementar llamada a Supabase
            // val response = supabase.from("delivery_active_orders")
            //     .select()
            //     .decodeList<ActiveOrder>()
            // activeOrders = response

            activeOrders = listOf()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    fun acceptOrder(orderId: String) {
        showComingSoonDialog = true
        // TODO: Implementar cuando tengas las coordenadas
        // scope.launch {
        //     try {
        //         supabase.rpc("accept_delivery_order") {
        //             parameter("order_id_param", orderId)
        //         }
        //         loadAvailableOrders()
        //         loadActiveOrders()
        //         selectedTab = 1
        //     } catch (e: Exception) {
        //         e.printStackTrace()
        //     }
        // }
    }

    suspend fun rejectOrder(orderId: String) {
        try {
            // TODO: Implementar llamada a Supabase
            // supabase.rpc("reject_delivery_order") {
            //     parameter("order_id_param", orderId)
            // }
            loadAvailableOrders()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        try {
            // TODO: Implementar llamada a Supabase
            // supabase.rpc("update_delivery_status") {
            //     parameter("order_id_param", orderId)
            //     parameter("new_status", newStatus)
            // }
            loadActiveOrders()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Cargar órdenes al iniciar
    LaunchedEffect(Unit) {
        loadAvailableOrders()
        loadActiveOrders()
    }

    // Callback para ubicación
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    val point = Point.fromLngLat(location.longitude, location.latitude)
                    userPoint = point

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

    // Setup de ubicación
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

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (isLocationEnabled(context)) {
                    showEnableLocationDialog = false
                    isTracking = false
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Disponibles (${availableOrders.size})") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Activas (${activeOrders.size})") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    0 -> AvailableOrdersList(
                        orders = availableOrders,
                        isLoading = isLoading,
                        onAccept = { orderId -> acceptOrder(orderId) },
                        onReject = { orderId ->
                            scope.launch { rejectOrder(orderId) }
                        }
                    )

                    1 -> ActiveOrdersList(
                        orders = activeOrders,
                        isLoading = isLoading,
                        onUpdateStatus = { orderId, status ->
                            scope.launch { updateOrderStatus(orderId, status) }
                        }
                    )
                }
            }
        },
        sheetPeekHeight = 400.dp,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState
            ) {
                MapEffect(userPoint) { mapView ->
                    val annotationApi = mapView.annotations
                    val pointManager = annotationApi.createPointAnnotationManager()
                    pointManager.deleteAll()

                    userPoint?.let { point ->
                        val options = PointAnnotationOptions()
                            .withPoint(point)
                            .withIconImage("delivery_marker")
                            .withIconSize(1.1)
                            .withIconAnchor(IconAnchor.BOTTOM)
                        pointManager.create(options)
                    }
                }
            }

            if (showEnableLocationDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Ubicación desactivada") },
                    text = { Text("Para continuar con el delivery necesitas activar la ubicación del dispositivo") },
                    confirmButton = {
                        TextButton(onClick = {
                            showEnableLocationDialog = false
                            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }) {
                            Text("Activar ubicación")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEnableLocationDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showComingSoonDialog) {
                AlertDialog(
                    onDismissRequest = { showComingSoonDialog = false },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = { Text("Próximamente") },
                    text = {
                        Text(
                            "La funcionalidad de aceptar órdenes estará disponible próximamente. " +
                                    "Primero necesitamos configurar las coordenadas de los negocios.",
                            textAlign = TextAlign.Center
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { showComingSoonDialog = false }) {
                            Text("Entendido")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AvailableOrdersList(
    orders: List<DeliveryOrder>,
    isLoading: Boolean,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeliveryDining,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = "No hay órdenes disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                AvailableOrderCard(
                    order = order,
                    onAccept = { onAccept(order.id) },
                    onReject = { onReject(order.id) }
                )
            }
        }
    }
}

@Composable
fun AvailableOrderCard(
    order: DeliveryOrder,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    var showRejectDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Orden #${order.id.take(8)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${order.total}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${order.item_count} productos",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Delivery: $${order.delivery_fee}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showRejectDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rechazar")
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Aceptar")
                }
            }
        }
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Confirmar rechazo") },
            text = { Text("¿Estás seguro de que quieres rechazar esta orden?") },
            confirmButton = {
                TextButton(onClick = {
                    showRejectDialog = false
                    onReject()
                }) {
                    Text("Rechazar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ActiveOrdersList(
    orders: List<ActiveOrder>,
    isLoading: Boolean,
    onUpdateStatus: (String, String) -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = "No tienes órdenes activas",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                ActiveOrderCard(
                    order = order,
                    onUpdateStatus = onUpdateStatus
                )
            }
        }
    }
}

@Composable
fun ActiveOrderCard(
    order: ActiveOrder,
    onUpdateStatus: (String, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (order.status) {
                "waiting_delivery" -> MaterialTheme.colorScheme.secondaryContainer
                "on_the_way" -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Orden #${order.id.take(8)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusChip(status = order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: $${order.total}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Delivery: $${order.delivery_fee}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (order.status) {
                "waiting_delivery" -> {
                    Button(
                        onClick = { onUpdateStatus(order.id, "on_the_way") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Recoger pedido")
                    }
                }

                "on_the_way" -> {
                    Button(
                        onClick = { onUpdateStatus(order.id, "delivered") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Marcar como entregado")
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (text, color) = when (status) {
        "waiting_delivery" -> "Esperando" to MaterialTheme.colorScheme.secondary
        "on_the_way" -> "En camino" to MaterialTheme.colorScheme.primary
        "delivered" -> "Entregado" to MaterialTheme.colorScheme.tertiary
        else -> status to MaterialTheme.colorScheme.onSurface
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
