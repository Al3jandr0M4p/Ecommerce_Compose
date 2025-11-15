package com.clay.ecommerce_compose.ui.screens.client.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.cart.ShoppingCart
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navController: NavHostController, cartViewModel: CartViewModel) {
    var query by remember { mutableStateOf(value = "") }
    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
                            ) {
                                IconButton(
                                    onClick = { navController.navigate(route = "userHome") },
                                    modifier = Modifier.size(size = 32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier.size(size = 30.dp)
                                    )
                                }

                                Text(
                                    text = "Buscar",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500),
                                    fontSize = 20.sp
                                )
                            }

                            ShoppingCart(navController = navController, cartViewModel = cartViewModel)
                        }

                        TextField(
                            value = query,
                            onValueChange = { query = it },
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Buscar en ${stringResource(id = R.string.app_name)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 18.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search, contentDescription = null
                                )
                            },
                            maxLines = 1,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(id = R.color.lightGrey),
                                unfocusedContainerColor = colorResource(id = R.color.lightGrey),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = 56.dp)
                                .clip(shape = CircleShape)
                                .clickable {},
                            shape = CircleShape
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = scrollState,
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(50) {
                Text("sii ${it + 3}")
            }
        }
    }
}
