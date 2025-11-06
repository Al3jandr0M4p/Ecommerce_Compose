package com.clay.ecommerce_compose.ui.screens.admin


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.clay.ecommerce_compose.ui.components.AdminButton
import com.clay.ecommerce_compose.ui.components.AdminCard
import com.clay.ecommerce_compose.ui.components.AdminDropdown
import com.clay.ecommerce_compose.ui.components.AdminTable
import com.clay.ecommerce_compose.ui.components.AdminTextField
import com.clay.ecommerce_compose.ui.components.ConfirmDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    onBack: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedUserIndex by remember { mutableStateOf(-1) }
    var searchQuery by remember { mutableStateOf("") }
    var filterRole by remember { mutableStateOf("Todos") }
    var filterStatus by remember { mutableStateOf("Todos") }

    // Datos de ejemplo
    val users = remember {
        mutableStateListOf(
            User("001", "Juan Pérez", "juan@email.com", "Cliente", "Activo"),
            User("002", "María López", "maria@email.com", "Negocio", "Activo"),
            User("003", "Carlos Díaz", "carlos@email.com", "Repartidor", "Activo"),
            User("004", "Ana Martínez", "ana@email.com", "Cajero", "Inactivo"),
            User("005", "Luis Gómez", "luis@email.com", "Cliente", "Activo")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Barra de acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lista de Usuarios",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                AdminButton(
                    text = "Nuevo Usuario",
                    onClick = { showCreateDialog = true },
                    icon = Icons.Default.Add
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Filtros y búsqueda
            AdminCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = "Buscar",
                        placeholder = "Nombre, email...",
                        leadingIcon = Icons.Default.Search,
                        modifier = Modifier.weight(1f)
                    )

                    AdminDropdown(
                        label = "Rol",
                        selectedValue = filterRole,
                        options = listOf("Todos", "Cliente", "Negocio", "Repartidor", "Cajero"),
                        onValueChange = { filterRole = it },
                        modifier = Modifier.weight(0.5f)
                    )

                    AdminDropdown(
                        label = "Estado",
                        selectedValue = filterStatus,
                        options = listOf("Todos", "Activo", "Inactivo"),
                        onValueChange = { filterStatus = it },
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabla de usuarios
            AdminCard(title = "Usuarios Registrados (${users.size})") {
                AdminTable(
                    headers = listOf("ID", "Nombre", "Email", "Rol", "Estado"),
                    data = users.map { user ->
                        listOf(user.id, user.name, user.email, user.role, user.status)
                    },
                    actions = { index ->
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = { /* Ver detalles */ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Visibility,
                                    "Ver",
                                    tint = Color(0xFF3498DB),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = { /* Editar */ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    "Editar",
                                    tint = Color(0xFFF39C12),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    selectedUserIndex = index
                                    showDeleteDialog = true
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    "Eliminar",
                                    tint = Color(0xFFE74C3C),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AdminCard(modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            tint = Color(0xFF3498DB),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Activos",
                            fontSize = 14.sp,
                            color = Color(0xFF7F8C8D)
                        )
                        Text(
                            text = users.count { it.status == "Activo" }.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF27AE60)
                        )
                    }
                }

                AdminCard(modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.PersonOff,
                            contentDescription = null,
                            tint = Color(0xFFE74C3C),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Inactivos",
                            fontSize = 14.sp,
                            color = Color(0xFF7F8C8D)
                        )
                        Text(
                            text = users.count { it.status == "Inactivo" }.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE74C3C)
                        )
                    }
                }
            }
        }
    }

    // Dialog crear usuario
    if (showCreateDialog) {
        CreateUserDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, email, role, password ->
                users.add(User(
                    id = (users.size + 1).toString().padStart(3, '0'),
                    name = name,
                    email = email,
                    role = role,
                    status = "Activo"
                ))
                showCreateDialog = false
            }
        )
    }

    // Dialog eliminar usuario
    ConfirmDialog(
        show = showDeleteDialog,
        title = "Eliminar Usuario",
        message = "¿Está seguro que desea eliminar este usuario? Esta acción no se puede deshacer.",
        onConfirm = {
            if (selectedUserIndex >= 0) {
                users.removeAt(selectedUserIndex)
            }
            showDeleteDialog = false
            selectedUserIndex = -1
        },
        onDismiss = {
            showDeleteDialog = false
            selectedUserIndex = -1
        }
    )
}

@Composable
fun CreateUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Crear Nuevo Usuario",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre completo",
                    placeholder = "Juan Pérez",
                    leadingIcon = Icons.Default.Person
                )

                AdminTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "usuario@email.com",
                    leadingIcon = Icons.Default.Email
                )

                AdminDropdown(
                    label = "Rol",
                    selectedValue = role,
                    options = listOf("Cliente", "Negocio", "Repartidor", "Cajero"),
                    onValueChange = { role = it }
                )

                AdminTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    placeholder = "********",
                    leadingIcon = Icons.Default.Lock
                )
            }
        },
        confirmButton = {
            AdminButton(
                text = "Crear",
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && role.isNotBlank() && password.isNotBlank()) {
                        onConfirm(name, email, role, password)
                    }
                }
            )
        },
        dismissButton = {
            AdminButton(
                text = "Cancelar",
                onClick = onDismiss,
                isSecondary = true
            )
        }
    )
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val status: String
)