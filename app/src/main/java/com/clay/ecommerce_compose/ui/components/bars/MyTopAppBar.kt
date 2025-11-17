package com.clay.ecommerce_compose.ui.components.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavHostController,
    isAtTop: Boolean? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    idShop: Int?
) {
    TopAppBar(
        title = {
            if (isAtTop == true) {
                Text(text = "")
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "nombre del negocio",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { navController.navigate(route = "searchInShop/${idShop}") },
                        modifier = Modifier.size(size = 50.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(route = "userHome")
            }, modifier = Modifier.size(size = 50.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
