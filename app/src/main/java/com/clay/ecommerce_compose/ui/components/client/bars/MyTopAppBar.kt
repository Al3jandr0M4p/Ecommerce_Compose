package com.clay.ecommerce_compose.ui.components.client.bars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavHostController, isAtTop: Boolean? = null, scrollBehavior: TopAppBarScrollBehavior? = null) {
    TopAppBar(
        title = {
            if (isAtTop == true) {
                Text(text = "")
            } else {
                TextField(
                    value = "",
                    onValueChange = {},
                    singleLine = true,
                    placeholder = { Text(text = "Buscar en el negocio...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        navigationIcon = {
            if (isAtTop == true) {
                IconButton(onClick = {
                    navController.navigate(route = "userHome")
                }, modifier = Modifier.size(size = 50.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
