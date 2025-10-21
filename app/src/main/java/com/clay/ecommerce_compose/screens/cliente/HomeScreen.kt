package com.clay.ecommerce_compose.screens.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.clay.ecommerce_compose.R

@Composable
fun HeaderUserHome() {
    Row {
        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = null)
        Column {
            Text(text = "Jose ")
        }
    }
}

@Composable
fun UserHomeScreen(modifier : Modifier = Modifier) {
    Column {

    }
}

@Preview(showBackground = true)
@Composable
fun UserHomeScreenPreview() {
    UserHomeScreen()
}
