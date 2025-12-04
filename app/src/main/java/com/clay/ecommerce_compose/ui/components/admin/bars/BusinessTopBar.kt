package com.clay.ecommerce_compose.ui.components.admin.bars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessTopBar(
    onBack: () -> Unit,
    pendingCount: Int
) {
    TopAppBar(
        title = { Text(text = "Negocios", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(color = 0xFF2C3E50),
            titleContentColor = Color.White
        ),
        actions = {
            if (pendingCount > 0) {
                Badge(
                    containerColor = Color(color = 0xFFE74C3C),
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = "$pendingCount",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    )
}
