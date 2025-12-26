package com.clay.ecommerce_compose.ui.screens.admin.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.ui.screens.admin.users.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onBack: () -> Unit
) {
    var showStatusDialog by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val orders = remember { mutableStateListOf<Order>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F6FA))
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF3498DB)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Todos") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Activos")
                            val activeCount = orders.count {
                                it.status !in listOf("Entregado", "Cancelado")
                            }
                            if (activeCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(containerColor = Color(0xFF3498DB)) {
                                    Text(activeCount.toString(), fontSize = 10.sp)
                                }
                            }
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Historial") }
                )
            }

            // Contenido según tab
            when (selectedTab) {
                0 -> AllOrdersList(
                    orders = orders,
                    onViewDetails = { order ->
                        selectedOrder = order
                        showDetailsDialog = true
                    },
                    onChangeStatus = { order ->
                        selectedOrder = order
                        showStatusDialog = true
                    }
                )
                1 -> ActiveOrdersList(
                    orders = orders.filter { it.status !in listOf("Entregado", "Cancelado") },
                    onChangeStatus = { order ->
                        selectedOrder = order
                        showStatusDialog = true
                    }
                )
                2 -> HistoryOrdersList(
                    orders = orders.filter { it.status in listOf("Entregado", "Cancelado") }
                )
            }
        }
    }

    // Dialog cambiar estado
    if (showStatusDialog && selectedOrder != null) {
        ChangeOrderStatusDialog(
            currentStatus = selectedOrder!!.status,
            onDismiss = {
                showStatusDialog = false
                selectedOrder = null
            },
            onConfirm = { newStatus ->
                val index = orders.indexOf(selectedOrder)
                if (index >= 0) {
                    orders[index] = selectedOrder!!.copy(status = newStatus)
                }
                showStatusDialog = false
                selectedOrder = null
            }
        )
    }

    // Dialog detalles
    if (showDetailsDialog && selectedOrder != null) {
        OrderDetailsDialog(
            order = selectedOrder!!,
            onDismiss = {
                showDetailsDialog = false
                selectedOrder = null
            }
        )
    }
}

@Composable
fun AllOrdersList(
    orders: List<Order>,
    onViewDetails: (Order) -> Unit,
    onChangeStatus: (Order) -> Unit
) {
    if (orders.isEmpty()) {
        EmptyState(
            icon = Icons.Default.ShoppingCart,
            message = "No hay pedidos registrados",
            actionText = "",
            onAction = { }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                OrderCard(
                    order = order,
                    onViewDetails = { onViewDetails(order) },
                    onChangeStatus = { onChangeStatus(order) }
                )
            }
        }
    }
}

@Composable
fun ActiveOrdersList(
    orders: List<Order>,
    onChangeStatus: (Order) -> Unit
) {
    if (orders.isEmpty()) {
        EmptyState(
            icon = Icons.Default.CheckCircle,
            message = "No hay pedidos activos",
            actionText = "",
            onAction = { }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                ActiveOrderCard(
                    order = order,
                    onChangeStatus = { onChangeStatus(order) }
                )
            }
        }
    }
}

@Composable
fun HistoryOrdersList(orders: List<Order>) {
    if (orders.isEmpty()) {
        EmptyState(
            icon = Icons.Default.History,
            message = "No hay historial de pedidos",
            actionText = "",
            onAction = { }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                HistoryOrderCard(order = order)
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onViewDetails: () -> Unit,
    onChangeStatus: () -> Unit
) {
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pedido #${order.id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = order.customer,
                        fontSize = 13.sp,
                        color = Color(0xFF7F8C8D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Detalles
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Store,
                    contentDescription = null,
                    tint = Color(0xFF95A5A6),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = order.business,
                    fontSize = 13.sp,
                    color = Color(0xFF7F8C8D)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF95A5A6),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = order.date,
                        fontSize = 12.sp,
                        color = Color(0xFF95A5A6)
                    )
                }

                Text(
                    text = order.total,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27AE60)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver")
                }

                Button(
                    onClick = onChangeStatus,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3498DB)
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Estado")
                }
            }
        }
    }
}

@Composable
fun ActiveOrderCard(
    order: Order,
    onChangeStatus: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (order.status) {
                "Pendiente" -> Color(0xFFFFF3CD)
                "Procesando" -> Color(0xFFD4EDFF)
                "En camino" -> Color(0xFFD1F2EB)
                else -> Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pedido #${order.id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = order.customer,
                        fontSize = 13.sp,
                        color = Color(0xFF7F8C8D)
                    )
                    Text(
                        text = order.business,
                        fontSize = 12.sp,
                        color = Color(0xFF95A5A6)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.total,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF27AE60)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onChangeStatus,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3498DB)
                )
            ) {
                Icon(Icons.Default.Update, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cambiar Estado")
            }
        }
    }
}

@Composable
fun HistoryOrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pedido #${order.id}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = order.customer,
                    fontSize = 12.sp,
                    color = Color(0xFF7F8C8D)
                )
                Text(
                    text = order.date,
                    fontSize = 11.sp,
                    color = Color(0xFF95A5A6)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = order.total,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27AE60)
                )
            }
        }
    }
}

@Composable
fun ChangeOrderStatusDialog(
    currentStatus: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar Estado", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Estado actual: $currentStatus",
                    fontSize = 13.sp,
                    color = Color(0xFF7F8C8D)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedStatus) }) {
                Text("Cambiar", color = Color(0xFF3498DB))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF95A5A6))
            }
        }
    )
}

@Composable
fun OrderDetailsDialog(
    order: Order,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pedido #${order.id}", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailRow("Cliente:", order.customer)
                DetailRow("Negocio:", order.business)
                DetailRow("Total:", order.total)
                DetailRow("Estado:", order.status)
                DetailRow("Fecha:", order.date)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color(0xFF3498DB))
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF7F8C8D))
        Text(value, fontSize = 13.sp, color = Color(0xFF2C3E50))
    }
}

data class Order(
    val id: String,
    val customer: String,
    val business: String,
    val total: String,
    val status: String,
    val date: String
)
