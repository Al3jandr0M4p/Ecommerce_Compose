package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.R

@Composable
fun RowScope.StepLine(isCompleted: Boolean) {
    Box(
        modifier = Modifier
            .weight(weight = 1f)
            .height(height = 2.dp)
            .then(
                other = if (isCompleted) {
                    Modifier.background(color = colorResource(id = R.color.black))
                } else {
                    Modifier
                        .background(color = colorResource(id = R.color.white))
                        .border(width = 1.dp, color = colorResource(id = R.color.black))
                }
            )
    )
}
