package com.clay.ecommerce_compose.ui.components.client.bars

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    val tabs = listOf(Tabs.Home, Tabs.Activity, Tabs.Configuration)

    NavigationBar(
        modifier = modifier.height(height = 90.dp),
        containerColor = colorResource(id = R.color.white)
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
                    Text(
                        text = tab.title,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(id = R.color.black),
                    unselectedIconColor = colorResource(id = R.color.coolGrey),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
