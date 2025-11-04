package com.clay.ecommerce_compose.ui.components.auth.users

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BottomComp(
    modifier: Modifier = Modifier,
    onButtonAction: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    shape: RoundedCornerShape,
    elevation: ButtonElevation,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onButtonAction,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = elevation,
        shape = shape,
        content = content
    )
}
