package com.clay.ecommerce_compose.ui.screens.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Configuration(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 14.dp)
        ) {
            Text(
                text = "Configuracion",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 28.sp
            )
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 14.dp)
                .clickable {
                    // TODO not yet implemented
                },
            shape = RoundedCornerShape(size = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(size = 50.dp)
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Column {
                    Text(
                        text = "Alejandro Alvares",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "william.henry.harrison@pet-store.com",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.weight(weight = 1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = 24.dp)
                )
            }
        }

        // SignOut
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 14.dp)
                .clickable {
                    // Acción: Navegar a la ruta que maneja el cierre de sesión
                    navController.navigate("signout")
                },
            shape = RoundedCornerShape(size = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Cerrar sesión",
                    modifier = Modifier.size(size = 24.dp)
                )
            }
        }
    }
}
