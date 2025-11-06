package com.clay.ecommerce_compose.ui.components.client.sliders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

@Composable
fun SliderBranding(
    color: Int,
    title: String,
    description: String,
    logo: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 180.dp)
                .background(
                    color = colorResource(id = color), shape = RoundedCornerShape(
                        size = 16.dp
                    )
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .width(width = 180.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.white)
                )

                Spacer(modifier = Modifier.height(height = 6.dp))

                Text(
                    text = description,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.white),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            Image(
                painter = painterResource(id = logo),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            )
        }
    }
}
