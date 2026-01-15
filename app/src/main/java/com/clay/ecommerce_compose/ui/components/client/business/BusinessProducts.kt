package com.clay.ecommerce_compose.ui.components.client.business

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ProductCard(
    title: String,
    price: Double,
    image: String?,
    onAddClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val windowSize = calculateWindowSizeClass(activity)
    val isTablet = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

    Card(
        modifier = modifier
            .then(
                other = if (isTablet) {
                    Modifier.width(width = 520.dp)
                } else {
                    Modifier.fillMaxWidth()
                }
            )
            .padding(horizontal = if (isTablet) 16.dp else 12.dp)
            .clickable {},
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isTablet) 2.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = if (isTablet) 180.dp else 160.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = if (isTablet) 20.sp else 18.sp,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatPrice(amount = price),
                    fontSize = if (isTablet) 18.sp else 14.sp,
                    style = MaterialTheme.typography.labelSmall
                )

                IconButton(
                    onClick = onAddClick,
                    enabled = enabled,
                    modifier = Modifier
                        .size(if (isTablet) 44.dp else 36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = colorResource(id = R.color.black),
                        disabledContainerColor = colorResource(id = R.color.lightGrey)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = colorResource(id = R.color.white),
                        modifier = Modifier.size(if (isTablet) 22.dp else 18.dp)
                    )
                }

            }
        }
    }
}
