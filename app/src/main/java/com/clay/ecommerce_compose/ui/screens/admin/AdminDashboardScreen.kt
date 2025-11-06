package com.clay.ecommerce_compose.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToUsers: () -> Unit,
    onNavigateToBusinesses: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel Admin",
                        fontWeight = FontWeight.Bold
                    )
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
                .padding(16.dp)
        ) {
            Text(
                text = "Bienvenido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Menu buttons
            SimpleMenuButton(
                title = "Usuarios",
                icon = Icons.Default.People,
                onClick = onNavigateToUsers
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleMenuButton(
                title = "Negocios",
                icon = Icons.Default.Store,
                onClick = onNavigateToBusinesses
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleMenuButton(
                title = "Productos",
                icon = Icons.Default.Inventory,
                onClick = onNavigateToProducts
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleMenuButton(
                title = "Categorías",
                icon = Icons.Default.Category,
                onClick = onNavigateToCategories
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleMenuButton(
                title = "Pedidos",
                icon = Icons.Default.ShoppingCart,
                onClick = onNavigateToOrders
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleMenuButton(
                title = "Reportes",
                icon = Icons.Default.Assessment,
                onClick = onNavigateToReports
            )
        }
    }
}

@Composable
fun SimpleMenuButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF3498DB),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}