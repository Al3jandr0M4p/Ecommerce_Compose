package com.clay.ecommerce_compose.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.ui.components.admin.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessScreen(
    onBack: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedBusiness by remember { mutableStateOf<Business?>(null) }
    var selectedTab by remember { mutableStateOf(0) }

    val businesses = remember { mutableStateListOf<Business>() }
    val pendingBusinesses = businesses.filter { it.status == "Pendiente" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Negocios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50),
                    titleContentColor = Color.White
                ),
                actions = {
                    if (pendingBusinesses.isNotEmpty()) {
                        Badge(
                            containerColor = Color(0xFFE74C3C),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = pendingBusinesses.size.toString(),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
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
                            Text("Pendientes")
                            if (pendingBusinesses.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(containerColor = Color(0xFFF39C12)) {
                                    Text(
                                        pendingBusinesses.size.toString(),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                )
            }

            // Contenido
            when (selectedTab) {
                0 -> AllBusinessesList(
                    businesses = businesses,
                    onEdit = { business ->
                        selectedBusiness = business
                        showEditDialog = true
                    },
                    onSuspend = { business ->
                        val index = businesses.indexOf(business)
                        if (index >= 0) {
                            businesses[index] = business.copy(status = "Suspendido")
                        }
                    },
                    onAdd = { showAddDialog = true }
                )
                1 -> PendingBusinessesList(
                    businesses = pendingBusinesses,
                    onApprove = { business ->
                        val index = businesses.indexOf(business)
                        if (index >= 0) {
                            businesses[index] = business.copy(status = "Aprobado")
                        }
                    },
                    onReject = { business ->
                        businesses.remove(business)
                    }
                )
            }
        }
    }

    // Dialog editar
    if (showEditDialog && selectedBusiness != null) {
        EditBusinessDialog(
            business = selectedBusiness!!,
            onDismiss = {
                showEditDialog = false
                selectedBusiness = null
            },
            onConfirm = { name, rnc, address ->
                val index = businesses.indexOf(selectedBusiness)
                if (index >= 0) {
                    businesses[index] = selectedBusiness!!.copy(
                        name = name,
                        rnc = rnc,
                        address = address
                    )
                }
                showEditDialog = false
                selectedBusiness = null
            }
        )
    }

    // Dialog agregar
    if (showAddDialog) {
        AddBusinessDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, rnc, address ->
                businesses.add(
                    Business(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        rnc = rnc,
                        address = address,
                        status = "Aprobado"
                    )
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AllBusinessesList(
    businesses: List<Business>,
    onEdit: (Business) -> Unit,
    onSuspend: (Business) -> Unit,
    onAdd: () -> Unit
) {
    if (businesses.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Store,
            message = "No hay negocios registrados",
            actionText = "Agregar Negocio",
            onAction = onAdd
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(businesses) { business ->
                BusinessCard(
                    business = business,
                    onEdit = { onEdit(business) },
                    onSuspend = { onSuspend(business) }
                )
            }
        }
    }
}

@Composable
fun PendingBusinessesList(
    businesses: List<Business>,
    onApprove: (Business) -> Unit,
    onReject: (Business) -> Unit
) {
    if (businesses.isEmpty()) {
        EmptyState(
            icon = Icons.Default.CheckCircle,
            message = "No hay solicitudes pendientes",
            actionText = "",
            onAction = { }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(businesses) { business ->
                PendingBusinessCard(
                    business = business,
                    onApprove = { onApprove(business) },
                    onReject = { onReject(business) }
                )
            }
        }
    }
}

@Composable
fun BusinessCard(
    business: Business,
    onEdit: () -> Unit,
    onSuspend: () -> Unit
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = Color(0xFF27AE60).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Store,
                                contentDescription = null,
                                tint = Color(0xFF27AE60),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = business.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                        Text(
                            text = "RNC: ${business.rnc}",
                            fontSize = 12.sp,
                            color = Color(0xFF7F8C8D)
                        )
                    }
                }

                StatusBadge(
                    text = business.status,
                    status = when (business.status) {
                        "Aprobado" -> "success"
                        "Pendiente" -> "warning"
                        else -> "danger"
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF95A5A6),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = business.address,
                    fontSize = 13.sp,
                    color = Color(0xFF7F8C8D)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editar")
                }

                OutlinedButton(
                    onClick = onSuspend,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFF39C12)
                    )
                ) {
                    Icon(
                        Icons.Default.Block,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Suspender")
                }
            }
        }
    }
}

@Composable
fun PendingBusinessCard(
    business: Business,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = business.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF856404)
                    )
                    Text(
                        text = "RNC: ${business.rnc}",
                        fontSize = 12.sp,
                        color = Color(0xFF856404)
                    )
                    Text(
                        text = business.address,
                        fontSize = 12.sp,
                        color = Color(0xFF856404)
                    )
                }

                StatusBadge(text = "PENDIENTE", status = "warning")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF27AE60)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Aprobar")
                }

                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE74C3C)
                    )
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Rechazar")
                }
            }
        }
    }
}

@Composable
fun EditBusinessDialog(
    business: Business,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(business.name) }
    var rnc by remember { mutableStateOf(business.rnc) }
    var address by remember { mutableStateOf(business.address) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Negocio", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = rnc,
                    onValueChange = { rnc = it },
                    label = { Text("RNC") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name, rnc, address) }) {
                Text("Guardar", color = Color(0xFF3498DB))
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
fun AddBusinessDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var rnc by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Negocio", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = name.isBlank()
                )
                OutlinedTextField(
                    value = rnc,
                    onValueChange = { rnc = it },
                    label = { Text("RNC") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = rnc.isBlank()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = address.isBlank()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    if(name.isNotBlank() && rnc.isNotBlank() && address.isNotBlank()){
                        onConfirm(name, rnc, address)
                    }
                }
            ) {
                Text("Agregar", color = Color(0xFF3498DB))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF95A5A6))
            }
        }
    )
}

data class Business(
    val id: String,
    val name: String,
    val rnc: String,
    val address: String,
    val status: String
)
