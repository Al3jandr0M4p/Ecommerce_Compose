package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Carrito",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }, navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(route = "userHome") },
                        modifier = Modifier.Companion.size(size = 45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.Companion.size(size = 30.dp)
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white))
        ) {
            item {
                Column(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_signin),
                        contentDescription = null,
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .height(height = 450.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.tu_carrito_esta_vacio_continua_disfrutando_nuestros_negocios),
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        maxLines = 2,
                        textAlign = TextAlign.Companion.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
