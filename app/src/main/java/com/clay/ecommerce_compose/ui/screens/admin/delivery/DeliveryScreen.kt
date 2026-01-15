package com.clay.ecommerce_compose.ui.screens.admin.delivery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.domain.model.Delivery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(
    onBack: () -> Unit,
    viewModel: DeliveryViewModel
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedDelivery by remember { mutableStateOf<Delivery?>(null) }

    val deliveries by viewModel.deliveries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deliveries", fontWeight = FontWeight.Bold) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Color(0xFF3498DB)
            ) {
                Icon(Icons.Default.Add, "Agregar", tint = Color.White)
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (deliveries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay deliveries registrados", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(deliveries) { delivery ->
                        DeliveryItem(
                            delivery = delivery,
                            onEdit = {
                                selectedDelivery = delivery
                                showEditDialog = true
                            },
                            onDelete = {
                                selectedDelivery = delivery
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Crear delivery
    if (showCreateDialog) {
        DeliveryDialog(
            title = "Nuevo Delivery",
            delivery = null,
            onDismiss = { showCreateDialog = false },
            onConfirm = { customer, person, phone ->
                viewModel.createDelivery(customer, person, phone)
                showCreateDialog = false
            }
        )
    }

    // Editar delivery
    if (showEditDialog && selectedDelivery != null) {
        DeliveryDialog(
            title = "Editar Delivery",
            delivery = selectedDelivery,
            onDismiss = {
                showEditDialog = false
                selectedDelivery = null
            },
            onConfirm = { customer, person, phone ->
                viewModel.updateDelivery(
                    selectedDelivery!!.id,
                    customer,
                    person,
                    phone
                )
                showEditDialog = false
                selectedDelivery = null
            }
        )
    }

    // Eliminar delivery
    if (showDeleteDialog && selectedDelivery != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                selectedDelivery = null
            },
            title = { Text("Eliminar Delivery") },
            text = {
                Text("¿Seguro que deseas eliminar el delivery de ${selectedDelivery!!.customer_name}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteDelivery(selectedDelivery!!.id)
                        showDeleteDialog = false
                        selectedDelivery = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    selectedDelivery = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun DeliveryItem(
    delivery: Delivery,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = delivery.customer_name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Orden: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = delivery.order_id,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Repartidor: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = delivery.delivery_person,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Teléfono: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = delivery.phone,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Badge de estado
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (delivery.status) {
                            "PENDING" -> Color(0xFFF39C12)
                            "IN_TRANSIT" -> Color(0xFF3498DB)
                            "DELIVERED" -> Color(0xFF27AE60)
                            "CANCELLED" -> Color(0xFFE74C3C)
                            else -> Color.Gray
                        }
                    ) {
                        Text(
                            text = when (delivery.status) {
                                "PENDING" -> "Pendiente"
                                "IN_TRANSIT" -> "En camino"
                                "DELIVERED" -> "Entregado"
                                "CANCELLED" -> "Cancelado"
                                else -> delivery.status
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3498DB)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar")
                }

                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE74C3C)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun DeliveryDialog(
    title: String,
    delivery: Delivery?,
    onDismiss: () -> Unit,
    onConfirm: (
        customer: String,
        person: String,
        phone: String
    ) -> Unit
) {
    var customer by remember { mutableStateOf(delivery?.customer_name ?: "") }
    var person by remember { mutableStateOf(delivery?.delivery_person ?: "") }
    var phone by remember { mutableStateOf(delivery?.phone ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = customer,
                    onValueChange = { customer = it },
                    label = { Text("Nombre del cliente *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = person,
                    onValueChange = { person = it },
                    label = { Text("Repartidor *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(customer, person, phone)
                },
                enabled = customer.isNotBlank() &&
                        person.isNotBlank() &&
                        phone.isNotBlank()
            ) {
                Text(if (delivery == null) "Crear" else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}