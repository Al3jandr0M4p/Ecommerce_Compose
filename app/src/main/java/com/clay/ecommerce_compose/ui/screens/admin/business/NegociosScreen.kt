package com.clay.ecommerce_compose.ui.screens.admin.negocios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.domain.model.Business
import com.clay.ecommerce_compose.domain.model.UserWithRole
import com.clay.ecommerce_compose.ui.components.admin.bars.BusinessTopBar
import com.clay.ecommerce_compose.utils.hooks.useNegocioAdminScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NegociosScreen(
    onBack: () -> Unit
) {
    val viewModel = useNegocioAdminScreen()
    val businesses by viewModel.businesses.collectAsState()
    val businessUsers by viewModel.businessUsers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedBusiness by remember { mutableStateOf<Business?>(null) }

    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    // Mostrar error si existe
    LaunchedEffect(error) {
        error?.let {
            // Aquí podrías mostrar un Snackbar o Toast
            println("Error: $it")
        }
    }

    Scaffold(
        topBar = {
            BusinessTopBar(
                onBack = onBack,
                pendingCount = 0
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF2C3E50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar negocio", tint = Color.White)
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
            } else if (businesses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay negocios registrados")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F6FA)),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(businesses) { business ->
                        BusinessItem(
                            business = business,
                            onEdit = { selectedBusiness = business },
                            onDelete = { viewModel.deleteBusiness(business.id!!) }
                        )
                    }
                }
            }
        }
    }

    // Crear negocio
    if (showAddDialog) {
        AddNegocioDialog(
            businessUsers = businessUsers,
            onDismiss = { showAddDialog = false },
            onConfirm = { ownerId, name, phone, category ->
                viewModel.createBusiness(ownerId, name, phone, category)
                showAddDialog = false
            }
        )
    }

    // Editar negocio
    if (selectedBusiness != null) {
        EditNegocioDialog(
            business = selectedBusiness!!,
            onDismiss = { selectedBusiness = null },
            onConfirm = { name, phone, category ->
                viewModel.updateBusiness(
                    selectedBusiness!!.id!!,
                    name,
                    phone,
                    category
                )
                selectedBusiness = null
            }
        )
    }
}

@Composable
fun BusinessItem(
    business: Business,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(business.name, style = MaterialTheme.typography.titleMedium)
                Text("Categoría: ${business.category}")
                Text("Tel: ${business.phone}")
            }

            Row {
                TextButton(onClick = onEdit) {
                    Text("Editar")
                }
                TextButton(onClick = onDelete) {
                    Text("Eliminar", color = Color.Red)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNegocioDialog(
    businessUsers: List<UserWithRole>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var selectedOwnerId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Negocio") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Dropdown para seleccionar el dueño del negocio
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = if (selectedOwnerId.isNotBlank()) {
                            // ✅ Mostrar el email del usuario seleccionado
                            businessUsers.find { it.id == selectedOwnerId }?.email
                                ?: "Usuario: ${selectedOwnerId.take(8)}..."
                        } else {
                            "Seleccionar dueño del negocio"
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Dueño del negocio *") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (selectedOwnerId.isBlank()) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        ),
                        isError = selectedOwnerId.isBlank()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (businessUsers.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            "No hay usuarios con rol de Negocio",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "Crea usuarios en la sección de Usuarios primero",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                },
                                onClick = { }
                            )
                        } else {
                            businessUsers.forEach { user ->
                                DropdownMenuItem(
                                    text = {
                                        Column(
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        ) {
                                            // ✅ Mostrar email como título principal
                                            Text(
                                                text = user.email ?: "Sin email",
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                            )
                                            // Mostrar ID abreviado como secundario
                                            Text(
                                                text = "ID: ${user.id.take(8)}...",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedOwnerId = user.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del negocio *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = name.isBlank()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = phone.isBlank()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = category.isBlank()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedOwnerId, name, phone, category) },
                enabled = selectedOwnerId.isNotBlank() &&
                        name.isNotBlank() &&
                        phone.isNotBlank() &&
                        category.isNotBlank()
            ) {
                Text("Crear")
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
fun EditNegocioDialog(
    business: Business,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(business.name) }
    var phone by remember { mutableStateOf(business.phone) }
    var category by remember { mutableStateOf(business.category) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Negocio") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, phone, category) },
                enabled = name.isNotBlank() && phone.isNotBlank() && category.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}