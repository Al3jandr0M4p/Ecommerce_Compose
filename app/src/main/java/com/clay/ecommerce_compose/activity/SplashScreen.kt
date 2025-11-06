package com.clay.ecommerce_compose.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusinessViewModel


@Composable
fun SplashScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val splashState by mainViewModel.splashState.collectAsState()
    LaunchedEffect(splashState) {

        if (splashState is SplashState.Success) {
            val session = (splashState as SplashState.Success).session

            val destination = if (session == null) {
                "login"
            } else {
                when (session.role) {
                    "admin" -> "adminHome"
                    "repartidor" -> "deliveryHome"
                    "usuario" -> "userHome"
                    "negocio" -> {
                        val businessId = session.businessId
                        if (businessId != null) "businessHome/${businessId}"
                        else "login"
                    }
                    else -> "login"
                }
            }


            navController.navigate(route = destination) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.black)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Click",
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.white),
                fontSize = 50.sp,
            )
            Box(modifier = Modifier.padding(start = 2.dp)) {
                Text(
                    text = "Market",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.white),
                    fontSize = 45.sp,
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = (-8).dp)
                        .size(size = 22.dp)
                        .background(
                            color = Color(color = 0xff06c167),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "2",
                        color = Color.White,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}
