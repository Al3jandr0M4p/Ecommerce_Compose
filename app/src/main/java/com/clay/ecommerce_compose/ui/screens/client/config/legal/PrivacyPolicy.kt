package com.clay.ecommerce_compose.ui.screens.client.config.legal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicy(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Términos y Condiciones",
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(route = "userHome") },
                        modifier = Modifier.Companion.size(size = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white))
                .padding(horizontal = 18.dp)
        ) {
            item {
                Text(
                    text = "Politicas de Privacidad",
                    fontSize = 20.sp,
                    lineHeight = 40.sp,
                    maxLines = 2,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.Companion.padding(top = 12.dp)
                )

                Text(
                    text = stringResource(
                        id = R.string.polity_privici_text,
                        stringResource(id = R.string.app_name)
                    ),
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    maxLines = 3,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.Companion.padding(vertical = 12.dp)
                )

                Text(
                    text = "Al usar el Servicio, aceptas la recopilación y el uso de información de acuerdo con esta\npolítica.",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 3,
                )
            }

            item {
                Text(
                    text = "Informacion que Recopilamos",
                    fontSize = 20.sp,
                    lineHeight = 40.sp,
                    maxLines = 2,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.Companion.padding(top = 12.dp)
                )

                Text(
                    text = "Podemos recopilar información que identifica al usuario, incluyendo pero no limitada a:",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    maxLines = 3,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.Companion.padding(vertical = 12.dp)
                )

                Text(
                    text = "Nombre" +
                            "\n" +
                            "Correo electrónico" +
                            "\n" +
                            "Número de teléfono" +
                            "\n" +
                            "Dirección" +
                            "\n" +
                            "Información de autenticación (por ejemplo, ID de usuario)" +
                            "\n" +
                            "Información proporcionada voluntariamente al registrarse o usar el Servicio",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            item {
                Text(
                    text = "Informacion no personal",
                    fontSize = 20.sp,
                    lineHeight = 40.sp,
                    maxLines = 2,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.Companion.padding(top = 12.dp)
                )

                Text(
                    text = "La información recopilada se utiliza para:",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.Companion.padding(vertical = 12.dp)
                )

                Text(
                    text = "Dirección IP\n" +
                            "Tipo de dispositivo\n" +
                            "Sistema operativo\n" +
                            "Identificadores del dispositivo\n" +
                            "Datos de uso, interacción y rendimiento\n" +
                            "Fecha y hora de acceso",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            item {
                Text(
                    text = "Uso de la Informacion",
                    fontSize = 20.sp,
                    lineHeight = 40.sp,
                    maxLines = 2,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.Companion.padding(top = 12.dp)
                )

                Text(
                    text = "Proveer y mantener el Servicio\n" +
                            "Mejorar la experiencia del usuario\n" +
                            "Gestionar cuentas y autenticación\n" +
                            "Procesar pedidos, pagos o solicitudes\n" +
                            "Enviar notificaciones relacionadas con el Servicio\n" +
                            "Detectar, prevenir y solucionar problemas técnicos o de seguridad\n" +
                            "Cumplir obligaciones legales",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            item {
                Text(
                    text = "Comparticion de la Informacion",
                    fontSize = 20.sp,
                    lineHeight = 40.sp,
                    maxLines = 2,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.Companion.padding(top = 12.dp)
                )

                Text(
                    text = "No vendemos ni alquilamos información personal.",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.Companion.padding(top = 12.dp)
                )

                Text(
                    text = "Podemos compartir información únicamente en los siguientes casos:",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.Companion.padding(bottom = 12.dp)
                )

                Text(
                    text = "Con proveedores de servicios que nos ayudan a operar el Servicio\n" +
                            "Para cumplir obligaciones legales o solicitudes gubernamentales\n" +
                            "Para proteger nuestros derechos, seguridad o propiedad\n" +
                            "En caso de fusión, adquisición o venta de activos",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.Companion.padding(bottom = 12.dp)
                )
            }
        }
    }
}
