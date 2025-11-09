package com.clay.ecommerce_compose.ui.components.client.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R

@Composable
fun ConfigHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        AsyncImage(
            model = "",
            contentDescription = null,
            modifier = Modifier
                .size(size = 80.dp)
                .clip(shape = RoundedCornerShape(size = 18.dp))
        )

        Spacer(modifier = Modifier.width(width = 18.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Alejandro Molle",
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(height = 4.dp))

            Text(
                text = "molle0711@gmail.com",
                fontSize = 14.sp,
                style = MaterialTheme.typography.labelSmall
            )

            Row(modifier = Modifier.padding(top = 2.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.black),
                        contentColor = colorResource(id = R.color.white)
                    )
                ) {
                    Text(
                        text = "Editar perfil",
                        color = colorResource(id = R.color.white),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.width(width = 4.dp))

                TextButton(onClick = {}) {
                    Text(
                        text = "${stringResource(id = R.string.app_name)} One",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorResource(id = R.color.gold)
                    )
                }
            }
        }

    }
}