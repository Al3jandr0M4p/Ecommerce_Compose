package com.clay.ecommerce_compose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.utils.getChips

@Composable
fun Chips() {
    val chips = getChips()

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(items = chips) { chip ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(width = 110.dp)
                    .height(height = 36.dp)
                    .clickable {
                        /* TODO not yet implemented */
                    }
                    .background(color = Color(color = 0xfff2f2f2), shape = CircleShape)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = chip.icon,
                        contentDescription = null
                    )
                    Text(
                        text = chip.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
