package com.clay.ecommerce_compose.ui.components.bars

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.navigation.Tabs

@Composable
fun MyBottomNavigationBar(
    selectedTab: Tabs,
    onTabSelected: (Tabs) -> Unit,
    tabs: List<Tabs>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .height(height = 80.dp)
            .clip(shape = RoundedCornerShape(topEnd = 26.dp, topStart = 26.dp)),
        containerColor = colorResource(id = R.color.lightGrey)
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    if (isSelected) {
                        Text(
                            text = tab.title,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(id = R.color.black),
                    unselectedIconColor = colorResource(id = R.color.coolGrey),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
