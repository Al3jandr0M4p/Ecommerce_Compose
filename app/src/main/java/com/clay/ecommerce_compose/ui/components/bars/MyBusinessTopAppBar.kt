package com.clay.ecommerce_compose.ui.components.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBusinessTopAppBar(businessName: String, scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = 50.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(modifier = Modifier.width(width = 10.dp))

                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { }
                    ) {
                        Text(
                            text = businessName,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W400),
                            fontSize = 20.sp,
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                        )
                    }

                    Text(text = stringResource(id = R.string.propietario))
                }
            }
        },

        actions = {
            IconButton(onClick = { }, modifier = Modifier.size(size = 35.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(id = R.string.notification),
                    modifier = Modifier.size(size = 25.dp)
                )
            }
        },

        scrollBehavior = scrollBehavior,
    )
}
