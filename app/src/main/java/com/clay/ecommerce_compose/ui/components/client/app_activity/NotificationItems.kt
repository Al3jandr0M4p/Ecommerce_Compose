package com.clay.ecommerce_compose.ui.components.client.app_activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.header.NotificationItem

@Composable
fun NotificationItems(
    notification: NotificationItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = notification.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = colorResource(id = R.color.black)
            )
            Text(
                text = notification.message,
                fontSize = 12.sp,
                color = colorResource(id = R.color.coolGreyLight),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
