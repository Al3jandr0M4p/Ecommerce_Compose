package com.clay.ecommerce_compose.ui.components.client.business

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R

@Composable
fun SearchInShop(idShop: Int?, navController: NavHostController) {
    var query by remember { mutableStateOf(value = "") }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.white))
                .padding(top = 22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(route = "details/${idShop}")
                    },
                    modifier = Modifier.size(size = 50.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                Text(
                    text = "Buscar en este negocio",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 18.sp
                )
            }
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        text = "Buscar en la tienda",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W500),
                        fontSize = 16.sp,
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
                    .padding(horizontal = 16.dp)
                    .height(height = 56.dp),
                shape = CircleShape
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_ingredients),
            contentDescription = null,
            modifier = Modifier
                .size(size = 140.dp)
                .align(Alignment.Center)
        )
    }
}
