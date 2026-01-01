package com.clay.ecommerce_compose.activity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusinessViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val splashState by mainViewModel.splashState.collectAsState()

    LaunchedEffect(splashState) {
        if (splashState is SplashState.Success) {
            delay(1000)

            val session = (splashState as SplashState.Success).session
            val destination = when {
                session == null -> "login"
                session.role == "admin" -> "adminHome"
                session.role == "repartidor" -> "delivery"
                session.role == "usuario" -> "userHome"
                session.role == "negocio" -> session.businessId?.let { "businessHome/$it" } ?: "login"
                else -> "login"
            }
            navController.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
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
            AnimatedTypingText(
                text = "Click",
                fontSize = 50,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.white)
            )

            Box(modifier = Modifier) {
                AnimatedTypingText(
                    text = "Market",
                    fontSize = 45,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.white),
                    startDelay = 600
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = (-8).dp)
                        .size(size = 22.dp)
                        .background(
                            color = Color(0xff06c167),
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

@Composable
fun AnimatedTypingText(
    text: String,
    fontSize: Int,
    style: TextStyle,
    color: Color,
    startDelay: Int = 0
) {
    Row {
        text.forEachIndexed { index, char ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(startDelay + index * 120L)
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(300)
                ) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(300)
                )
            ) {
                Text(
                    text = char.toString(),
                    style = style,
                    color = color,
                    fontSize = fontSize.sp
                )
            }
        }
    }
}
