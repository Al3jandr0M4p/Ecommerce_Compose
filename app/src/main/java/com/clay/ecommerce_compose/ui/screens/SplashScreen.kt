package com.clay.ecommerce_compose.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(modifier: Modifier = Modifier, onSplashChange: () -> Unit = {}) {
    val alpha = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 1500))
        delay(timeMillis = 3000)
        alpha.animateTo(targetValue = 0f, animationSpec = tween(durationMillis = 1500))
        onSplashChange()
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
