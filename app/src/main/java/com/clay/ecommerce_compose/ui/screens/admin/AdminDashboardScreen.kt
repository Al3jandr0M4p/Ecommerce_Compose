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
    onNavigateToBusinesses: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToDelivery: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel Admin",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Notificaciones */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color.White
                        )
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Bienvenido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Text(
                text = "Gestiona tu plataforma",
                fontSize = 14.sp,
                color = Color(0xFF7F8C8D),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Stats Cards en Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniStatsCard(
                    title = "Negocios",
                    value = "0",
                    icon = Icons.Default.Store,
                    color = Color(0xFF27AE60),
                    modifier = Modifier.weight(1f)
                )
                MiniStatsCard(
                    title = "Delivery",
                    value = " ",
                    icon = Icons.Default.LocalShipping,
                    color = Color(0xFF03A9F4),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniStatsCard(
                    title = "Reportes",
                    value = " ",
                    icon = Icons.Default.Assessment,
                    color = Color(0xFFE74C3C),
                    modifier = Modifier.weight(1f)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Título sección
            Text(
                text = "Gestión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Menú de opciones
            MenuOption(
                icon = Icons.Default.Store,
                title = "Negocios",
                subtitle = "Administrar comercios",
                color = Color(0xFF27AE60),
                onClick = onNavigateToBusinesses
            )

            Spacer(modifier = Modifier.height(10.dp))

            MenuOption(
                icon = Icons.Default.LocalShipping,
                title = "Delivery",
                subtitle = "Aquí podrás ver todos los delivery",
                color = Color(0xFF03A9F4),
                onClick = onNavigateToDelivery
            )

            Spacer(modifier = Modifier.height(10.dp))

            MenuOption(
                icon = Icons.Default.Assessment,
                title = "Reportes",
                subtitle = "Estadísticas y reportes",
                color = Color(0xFFE74C3C),
                onClick = onNavigateToReports
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )

            Column {
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MenuOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF7F8C8D)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFBDC3C7)
            )
        }
    }
}
