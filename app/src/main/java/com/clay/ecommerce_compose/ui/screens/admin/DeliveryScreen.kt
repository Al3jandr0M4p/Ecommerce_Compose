package com.clay.ecommerce_compose.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de datos para Delivery
data class Delivery(
    val id: String,
    val orderId: String,
    val customerName: String,
    val deliveryPerson: String,
    val status: DeliveryStatus,
    val address: String,
    val phone: String,
    val total: String
)

enum class DeliveryStatus(val displayName: String, val color: Color) {
    PENDING("Pendiente", Color(0xFFF39C12)),
    IN_TRANSIT("En Camino", Color(0xFF3498DB)),
    DELIVERED("Entregado", Color(0xFF27AE60)),
    CANCELLED("Cancelado", Color(0xFFE74C3C))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(onBack: () -> Unit) {
    // Lista vacía - conectar con Supabase
    val deliveries = remember { mutableStateListOf<Delivery>() }
    var selectedFilter by remember { mutableStateOf<DeliveryStatus?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Gestión de Delivery",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF3498DB),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar delivery")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F6FA))
        ) {
            // Estadísticas rápidas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DeliveryStatCard(
                    title = "Pendientes",
                    count = deliveries.count { it.status == DeliveryStatus.PENDING }.toString(),
                    color = Color(0xFFF39C12),
                    modifier = Modifier.weight(1f)
                )
                DeliveryStatCard(
                    title = "En Ruta",
                    count = deliveries.count { it.status == DeliveryStatus.IN_TRANSIT }.toString(),
                    color = Color(0xFF3498DB),
                    modifier = Modifier.weight(1f)
                )
                DeliveryStatCard(
                    title = "Entregados",
                    count = deliveries.count { it.status == DeliveryStatus.DELIVERED }.toString(),
                    color = Color(0xFF27AE60),
                    modifier = Modifier.weight(1f)
                )
            }

            // Filtros con scroll horizontal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("Todos") }
                )
                FilterChip(
                    selected = selectedFilter == DeliveryStatus.PENDING,
                    onClick = { selectedFilter = DeliveryStatus.PENDING },
                    label = { Text("Pendiente") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeliveryStatus.PENDING.color.copy(alpha = 0.2f)
                    )
                )
                FilterChip(
                    selected = selectedFilter == DeliveryStatus.IN_TRANSIT,
                    onClick = { selectedFilter = DeliveryStatus.IN_TRANSIT },
                    label = { Text("En Camino") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeliveryStatus.IN_TRANSIT.color.copy(alpha = 0.2f)
                    )
                )
                FilterChip(
                    selected = selectedFilter == DeliveryStatus.DELIVERED,
                    onClick = { selectedFilter = DeliveryStatus.DELIVERED },
                    label = { Text("Entregado") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeliveryStatus.DELIVERED.color.copy(alpha = 0.2f)
                    )
                )
            }

            // Lista de deliveries o mensaje vacío
            if (deliveries.isEmpty()) {
                // Estado vacío
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeliveryDining,
                            contentDescription = null,
                            tint = Color(0xFFBDC3C7),
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay deliveries",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7F8C8D)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Los pedidos para entregar aparecerán aquí",
                            fontSize = 14.sp,
                            color = Color(0xFFBDC3C7),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                val filteredDeliveries = if (selectedFilter == null) {
                    deliveries
                } else {
                    deliveries.filter { it.status == selectedFilter }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredDeliveries) { delivery ->
                        DeliveryCard(
                            delivery = delivery,
                            onStatusChange = { newStatus ->
                                val index = deliveries.indexOf(delivery)
                                if (index != -1) {
                                    deliveries[index] = delivery.copy(status = newStatus)
                                }
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        // Diálogo para agregar delivery
        if (showAddDialog) {
            AddDeliveryDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { newDelivery ->
                    deliveries.add(newDelivery)
                    showAddDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeliveryDialog(
    onDismiss: () -> Unit,
    onAdd: (Delivery) -> Unit
) {
    var orderId by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var deliveryPerson by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Agregar Delivery",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = orderId,
                    onValueChange = { orderId = it },
                    label = { Text("ID de Orden") },
                    placeholder = { Text("ORD-001") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Nombre del Cliente") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = deliveryPerson,
                    onValueChange = { deliveryPerson = it },
                    label = { Text("Repartidor") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    placeholder = { Text("809-555-1234") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = total,
                    onValueChange = { total = it },
                    label = { Text("Total") },
                    placeholder = { Text("1250.00") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (orderId.isNotBlank() && customerName.isNotBlank()) {
                        val newDelivery = Delivery(
                            id = System.currentTimeMillis().toString(),
                            orderId = orderId,
                            customerName = customerName,
                            deliveryPerson = deliveryPerson.ifBlank { "Sin asignar" },
                            status = DeliveryStatus.PENDING,
                            address = address,
                            phone = phone,
                            total = "RD$ ${total}"
                        )
                        onAdd(newDelivery)
                    }
                },
                enabled = orderId.isNotBlank() && customerName.isNotBlank()
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DeliveryStatCard(
    title: String,
    count: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF7F8C8D)
            )
        }
    }
}

@Composable
fun DeliveryCard(
    delivery: Delivery,
    onStatusChange: (DeliveryStatus) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con ID de orden y status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = delivery.orderId,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )

                Box {
                    Surface(
                        color = delivery.status.color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.clickable { showMenu = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = delivery.status.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = delivery.status.color
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = delivery.status.color,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DeliveryStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.displayName) },
                                onClick = {
                                    onStatusChange(status)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información del cliente
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF7F8C8D),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = delivery.customerName,
                    fontSize = 14.sp,
                    color = Color(0xFF2C3E50)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Repartidor
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DeliveryDining,
                    contentDescription = null,
                    tint = Color(0xFF7F8C8D),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = delivery.deliveryPerson,
                    fontSize = 14.sp,
                    color = Color(0xFF2C3E50)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dirección
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF7F8C8D),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = delivery.address,
                    fontSize = 13.sp,
                    color = Color(0xFF7F8C8D),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color(0xFFECF0F1))

            Spacer(modifier = Modifier.height(12.dp))

            // Footer con total y acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total",
                        fontSize = 12.sp,
                        color = Color(0xFF7F8C8D)
                    )
                    Text(
                        text = delivery.total,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF27AE60)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { /* TODO: Llamar al cliente */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3498DB).copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Llamar",
                            tint = Color(0xFF3498DB),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { /* TODO: Ver en mapa */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF27AE60).copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Mapa",
                            tint = Color(0xFF27AE60),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}