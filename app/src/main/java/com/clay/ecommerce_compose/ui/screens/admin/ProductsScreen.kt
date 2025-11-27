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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onBack: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val products = remember { mutableStateListOf<Product>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F6FA))
        ) {
            // Buscador
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar producto") },
                        placeholder = { Text("Nombre...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Stats rápidas
            if (products.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickStatCard(
                        title = "Total",
                        value = products.size.toString(),
                        color = Color(0xFF3498DB),
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatCard(
                        title = "Stock Bajo",
                        value = products.count { (it.stock.toIntOrNull() ?: 0) < 50 }.toString(),
                        color = Color(0xFFF39C12),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Lista de productos
            if (products.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.Inventory,
                    message = "No hay productos registrados",
                    actionText = "Agregar Producto",
                    onAction = { showCreateDialog = true }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onEdit = {
                                selectedProduct = product
                                showEditDialog = true
                            },
                            onDelete = {
                                selectedProduct = product
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    if (showCreateDialog) {
        CreateProductDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, category, business, price, stock ->
                products.add(Product(
                    id = (products.size + 1).toString().padStart(3, '0'),
                    name = name,
                    category = category,
                    business = business,
                    price = price,
                    stock = stock
                ))
                showCreateDialog = false
            }
        )
    }

    if (showEditDialog && selectedProduct != null) {
        EditProductDialog(
            product = selectedProduct!!,
            onDismiss = {
                showEditDialog = false
                selectedProduct = null
            },
            onConfirm = { name, category, business, price, stock ->
                val index = products.indexOf(selectedProduct)
                if (index >= 0) {
                    products[index] = selectedProduct!!.copy(
                        name = name,
                        category = category,
                        business = business,
                        price = price,
                        stock = stock
                    )
                }
                showEditDialog = false
                selectedProduct = null
            }
        )
    }

    ConfirmDialog(
        show = showDeleteDialog,
        title = "Eliminar Producto",
        message = "¿Está seguro que desea eliminar ${selectedProduct?.name}?",
        onConfirm = {
            selectedProduct?.let { products.remove(it) }
            showDeleteDialog = false
            selectedProduct = null
        },
        onDismiss = {
            showDeleteDialog = false
            selectedProduct = null
        }
    )
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = color
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isLowStock = (product.stock.toIntOrNull() ?: 0) < 50

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isLowStock) Color(0xFFFFF3CD) else Color.White
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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isLowStock) Color(0xFF856404) else Color(0xFF2C3E50)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Category,
                            contentDescription = null,
                            tint = Color(0xFF95A5A6),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.category,
                            fontSize = 12.sp,
                            color = Color(0xFF7F8C8D)
                        )
                    }

                    Text(
                        text = product.business,
                        fontSize = 12.sp,
                        color = Color(0xFF7F8C8D)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = product.price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF27AE60)
                    )

                    StatusBadge(
                        text = "Stock: ${product.stock}",
                        status = if (isLowStock) "warning" else "success"
                    )
                }
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
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE74C3C)
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun CreateProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var business by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Producto", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                AdminDropdown(
                    label = "Categoría",
                    selectedValue = category,
                    options = listOf("Lácteos", "Panadería", "Medicamentos", "Granos", "Carnes", "Bebidas", "Limpieza"),
                    onValueChange = { category = it }
                )

                AdminDropdown(
                    label = "Negocio",
                    selectedValue = business,
                    options = listOf("Supermercado El Sol", "Panadería El Pan", "Farmacia La Salud", "Colmado Mi Barrio"),
                    onValueChange = { business = it }
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    placeholder = { Text("RD$0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    placeholder = { Text("0") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && category.isNotBlank() &&
                        business.isNotBlank() && price.isNotBlank() && stock.isNotBlank()) {
                        onConfirm(name, category, business, price, stock)
                    }
                }
            ) {
                Text("Crear", color = Color(0xFF3498DB))
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
fun EditProductDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var category by remember { mutableStateOf(product.category) }
    var business by remember { mutableStateOf(product.business) }
    var price by remember { mutableStateOf(product.price) }
    var stock by remember { mutableStateOf(product.stock) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Producto", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                AdminDropdown(
                    label = "Categoría",
                    selectedValue = category,
                    options = listOf("Lácteos", "Panadería", "Medicamentos", "Granos", "Carnes", "Bebidas", "Limpieza"),
                    onValueChange = { category = it }
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name, category, business, price, stock) }) {
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

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val business: String,
    val price: String,
    val stock: String
)