package com.clay.ecommerce_compose.ui.components.client.business

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.R

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsButtonIcons(
    icon: ImageVector,
    clickable: () -> Unit = {},
    firstColor: Color = Color(color = 0x66000000),
    secondColor: Color = Color(color = 0x88000000),
    tintColor: Color = colorResource(id = R.color.white),
    modifier: Modifier
) {
    IconButton(
        onClick = clickable,
        modifier = modifier
    ) {
        Box(modifier = Modifier.size(size = 40.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(firstColor, secondColor)
                        ),
                        shape = CircleShape
                    )
                    .graphicsLayer {
                        renderEffect = android.graphics.RenderEffect
                            .createBlurEffect(
                                25f,
                                25f,
                                android.graphics.Shader.TileMode.CLAMP
                            )
                            .asComposeRenderEffect()
                    }
            )

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tintColor,
            )
        }
    }
}