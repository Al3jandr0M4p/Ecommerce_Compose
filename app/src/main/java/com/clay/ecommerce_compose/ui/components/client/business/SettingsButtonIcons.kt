package com.clay.ecommerce_compose.ui.components.client.business

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.R

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsButtonIcons(icon: ImageVector, clickable: (() -> Unit)? = {}) {
    IconButton(
        onClick = { clickable?.invoke() },
        modifier = Modifier
            .size(size = 40.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(color = 0x44000000),
                        Color(color = 0x77000000)
                    ),
                ),
                shape = CircleShape
            )
            .graphicsLayer {
                android.graphics.RenderEffect.createBlurEffect(
                    40f,
                    40f,
                    android.graphics.Shader.TileMode.CLAMP
                )
                alpha = 0.8f
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(id = R.color.white)
        )
    }
}