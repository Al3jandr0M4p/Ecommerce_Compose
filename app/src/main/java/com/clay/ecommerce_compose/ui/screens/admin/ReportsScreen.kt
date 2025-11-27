package com.clay.ecommerce_compose.ui.screens.admin

import androidx.compose.foundation.background
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
import com.clay.ecommerce_compose.ui.components.admin.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBack: () -> Unit
) {
    var selectedReportType by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf("PDF") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Centro de Reportes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Text(
                text = "Genera reportes detallados de la actividad",
                fontSize = 13.sp,
                color = Color(0xFF7F8C8D),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Stats principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniStatsCard(
                    title = "Ventas",
                    value = "RD$0",
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF27AE60),
                    modifier = Modifier.weight(1f)
                )
                MiniStatsCard(
                    title = "Pedidos",
                    value = "0",
                    icon = Icons.Default.ShoppingCart,
                    color = Color(0xFF3498DB),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniStatsCard(
                    title = "Impuestos",
                    value = "RD$0",
                    icon = Icons.Default.AccountBalance,
                    color = Color(0xFF9B59B6),
                    modifier = Modifier.weight(1f)
                )
                MiniStatsCard(
                    title = "Clientes",
                    value = "0",
                    icon = Icons.Default.People,
                    color = Color(0xFFF39C12),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Generador de reportes
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Generar Reporte",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )

                    AdminDropdown(
                        label = "Tipo de Reporte",
                        selectedValue = selectedReportType,
                        options = listOf(
                            "Ventas Globales",
                            "Impuestos Recaudados",
                            "Inventario General",
                            "Clientes Registrados",
                            "Negocios Activos",
                            "Productos Más Vendidos"
                        ),
                        onValueChange = { selectedReportType = it }
                    )

                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Fecha Inicio") },
                        placeholder = { Text("DD/MM/AAAA") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("Fecha Fin") },
                        placeholder = { Text("DD/MM/AAAA") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    AdminDropdown(
                        label = "Formato",
                        selectedValue = selectedFormat,
                        options = listOf("PDF", "Excel", "CSV"),
                        onValueChange = { selectedFormat = it }
                    )

                    Button(
                        onClick = { /* TODO: Generar reporte */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3498DB)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generar Reporte")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reportes rápidos
            Text(
                text = "Reportes Rápidos",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(12.dp))

            QuickReportCard(
                title = "Ventas del Día",
                description = "Reporte de ventas de hoy",
                icon = Icons.Default.Today,
                color = Color(0xFF3498DB),
                onClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(10.dp))

            QuickReportCard(
                title = "Ventas de la Semana",
                description = "Últimos 7 días",
                icon = Icons.Default.DateRange,
                color = Color(0xFF27AE60),
                onClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(10.dp))

            QuickReportCard(
                title = "Productos Bajo Stock",
                description = "Inventario crítico",
                icon = Icons.Default.Warning,
                color = Color(0xFFF39C12),
                onClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(10.dp))

            QuickReportCard(
                title = "Negocios Pendientes",
                description = "Solicitudes sin aprobar",
                icon = Icons.Default.Pending,
                color = Color(0xFFE74C3C),
                onClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MiniStatsCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier
) {
    TODO("Not yet implemented")
}

@Composable
fun QuickReportCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = color.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF7F8C8D)
                )
            }

            Icon(
                Icons.Default.Download,
                contentDescription = null,
                tint = color
            )
        }
    }
}