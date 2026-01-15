package com.clay.ecommerce_compose.ui.screens.client.config

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.config.ConfigHeader
import com.clay.ecommerce_compose.ui.components.client.config.UtilsHeader

@Composable
fun Configuration(navController: NavHostController, configViewModel: ConfigViewModel) {
    LaunchedEffect(Unit) {
        configViewModel.getUserInfoById()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white))
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Cuenta",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W500),
                    fontSize = 40.sp,
                )
            }
        }

        item { ConfigHeader(viewModel = configViewModel) }

        item { UtilsHeader() }
        
        item { Spacer(modifier = Modifier.height(height = 8.dp)) }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {}
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )

                Column {
                    Text(
                        text = "Configuracion",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500)
                    )
                    Spacer(modifier = Modifier.height(height = 2.dp))
                    Text(
                        text = "Configuracion de la app y cuenta del usuario",
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )

                Text(
                    text = "Favoritos de ${stringResource(id = R.string.app_name)}",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500)
                )
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(route = "legal")
                    }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationImportant,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )

                Column {
                    Text(
                        text = "Legal",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500)
                    )
                    Spacer(modifier = Modifier.height(height = 2.dp))
                    Text(
                        text = "Terminos y condiciones de uso de la app ${stringResource(id = R.string.app_name)}",
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        configViewModel.signOut()

                        navController.navigate(route = "login") {
                            popUpTo(id = navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Output,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )

                Text(
                    text = "Cerrar sesion",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500)
                )
            }
        }
    }
}
