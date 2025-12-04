package com.clay.ecommerce_compose.ui.components.admin.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.ui.screens.admin.dashboard.DrawerMenuItem

@Composable
fun DrawerContent(
    onNavigateToUsers: () -> Unit,
    onNavigateToBusinesses: () -> Unit,
    onNavigateToDelivery: () -> Unit,
    onNavigateToReports: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.width(280.dp)
    ) {
        // Header del Drawer
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF2C3E50)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Icon(
                    Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Panel Admin",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "admin@example.com",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sección de Gestión
        Text(
            text = "GESTIÓN",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        DrawerMenuItem(
            icon = Icons.Default.People,
            title = "Usuarios",
            onClick = onNavigateToUsers
        )

        DrawerMenuItem(
            icon = Icons.Default.Store,
            title = "Negocios",
            onClick = onNavigateToBusinesses
        )

        DrawerMenuItem(
            icon = Icons.Default.LocalShipping,
            title = "Delivery",
            onClick = onNavigateToDelivery
        )

        DrawerMenuItem(
            icon = Icons.Default.Assessment,
            title = "Reportes",
            onClick = onNavigateToReports
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        Spacer(modifier = Modifier.weight(1f))

        // Cerrar sesión al final
        DrawerMenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = "Cerrar sesión",
            onClick = {
                onCloseDrawer()
                // TODO: Implementar cierre de sesión
            },
            color = Color(0xFFE74C3C)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
