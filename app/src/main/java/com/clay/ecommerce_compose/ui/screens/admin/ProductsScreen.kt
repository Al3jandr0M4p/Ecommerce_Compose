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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.ui.components.AdminButton
import com.clay.ecommerce_compose.ui.components.AdminCard
import com.clay.ecommerce_compose.ui.components.AdminDropdown
import com.clay.ecommerce_compose.ui.components.AdminTable
import com.clay.ecommerce_compose.ui.components.AdminTextField
import com.clay.ecommerce_compose.ui.components.ConfirmDialog
import com.clay.ecommerce_compose.ui.components.StatsCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Gestión de Productos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Lista de productos aquí...")
        }
    }
}