package com.clay.ecommerce_compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R


@Composable
fun NoInternetScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_wifi_off),
            contentDescription = null,
            modifier = Modifier.size(size = 60.dp)
        )

        Text(
            text = "No hay conexion a Internet",
            style = MaterialTheme.typography.labelMedium,
            fontSize = 20.sp
        )

        Text(
            text = "Por favor, revisa tu conexion a Internet",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 16.sp
        )
    }
}
