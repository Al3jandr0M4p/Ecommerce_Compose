package com.clay.ecommerce_compose.ui.components.auth.registerFooter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R

@Composable
fun BoxScope.FooterRegister(navController: NavHostController) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tienes un negocio",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Registralo aqui",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.focusedPurple),
                modifier = Modifier.clickable {
                    navController.navigate(route = "registerBusiness")
                })
            Spacer(modifier = Modifier.height(height = 20.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Eres un delivery independiente",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Registrate aqui",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.focusedPurple),
                modifier = Modifier.clickable {
                    navController.navigate(route = "registerDelivery")
                })
            Spacer(modifier = Modifier.height(height = 20.dp))
        }
    }
}