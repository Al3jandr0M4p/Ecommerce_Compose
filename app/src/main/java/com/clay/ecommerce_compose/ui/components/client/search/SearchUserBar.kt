package com.clay.ecommerce_compose.ui.components.client.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R

@Composable
fun SearchBarContainer(navController: NavHostController) {
    var text by remember { mutableStateOf(value = "") }

    TextField(
        value = text,
        onValueChange = {},
        placeholder = {
            Text(
                text = "Buscar en Click Marquet",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W500),
                fontSize = 16.sp,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = null
            )
        },
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.lightGrey),
            unfocusedContainerColor = colorResource(id = R.color.lightGrey),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            disabledContainerColor = colorResource(id = R.color.lightGrey),
            disabledIndicatorColor = Color.Transparent,
            disabledLeadingIconColor = colorResource(id = R.color.black),
            disabledPlaceholderColor = colorResource(id = R.color.coolGrey),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(height = 56.dp)
            .clip(shape = CircleShape)
            .clickable {
                navController.navigate(route = "search")
            },
        shape = CircleShape,
        enabled = false
    )
}

