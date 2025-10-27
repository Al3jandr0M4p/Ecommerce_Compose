package com.clay.ecommerce_compose.ui.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.clay.ecommerce_compose.MainViewModel
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.SplashState


@Composable
fun SplashScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val alpha = remember { Animatable(initialValue = 0f) }

    val splashState by mainViewModel.splashState.collectAsState()

    LaunchedEffect(splashState) {

        if (splashState is SplashState.Success) {
            val session = (splashState as SplashState.Success).session

            Log.d("SplashScreen", "Session: $session")
            Log.e("SplashScreen", "Session: $session")
            val destination = if (session == null) {
                "login"
            } else {
                when (session.role) {
                    "admin" -> "adminHome"
                    "repartidor" -> "deliveryHome"
                    "usuario" -> "userHome"
                    "vendedor" -> "sellerHome"
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
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_seller_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = 40.sp,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
