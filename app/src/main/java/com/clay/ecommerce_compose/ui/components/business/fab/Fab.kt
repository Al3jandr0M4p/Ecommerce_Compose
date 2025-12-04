package com.clay.ecommerce_compose.ui.components.business.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.navigation.Tabs

@Composable
fun Fab(selectedTab: Tabs, openSheet: MutableState<Boolean>) {
    AnimatedVisibility(visible = selectedTab is Tabs.Stock) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(
                onClick = { openSheet.value = true },
                modifier = Modifier.width(width = 200.dp),
                shape = CircleShape,
                containerColor = colorResource(id = R.color.black),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size = 34.dp)
                            .clip(shape = CircleShape)
                    )
                    Text(
                        text = "Agregar producto",
                        color = colorResource(id = R.color.white),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
