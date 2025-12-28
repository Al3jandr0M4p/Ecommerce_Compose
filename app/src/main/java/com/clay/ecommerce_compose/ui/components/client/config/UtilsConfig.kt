package com.clay.ecommerce_compose.ui.components.client.config

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

@Composable
fun UtilBoxHeader(icon: ImageVector, title: String, func: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .clickable { func.invoke() }
            .background(
                color = colorResource(id = R.color.lightGrey),
                shape = RoundedCornerShape(size = 12.dp)
            )
            .padding(all = 16.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(size = 30.dp)
        )

        Text(
            text = title,
            fontSize = 16.sp,
            style = MaterialTheme.typography.labelSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
fun UtilsHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        UtilBoxHeader(
            icon = Icons.AutoMirrored.Filled.Help,
            title = stringResource(id = R.string.help),
            func = {},
            modifier = Modifier.weight(weight = 1f))
        UtilBoxHeader(
            icon = Icons.Default.Wallet,
            title = stringResource(id = R.string.monedero),
            func = {},
            modifier = Modifier.weight(weight = 1f))
        UtilBoxHeader(
            icon = Icons.Default.Email,
            title = stringResource(id = R.string.bandeja_de_entrada),
            func = {},
            modifier = Modifier.weight(weight = 1f))
    }
}
