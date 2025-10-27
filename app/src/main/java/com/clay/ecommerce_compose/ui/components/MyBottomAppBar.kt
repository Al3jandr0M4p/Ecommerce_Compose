package com.clay.ecommerce_compose.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.data.Tabs

@Composable
fun MyBotomAppBar(selectedTab: Tabs, onTabSelected: (Tabs) -> Unit) {
    NavigationBar {
        val tabs = listOf(Tabs.Home, Tabs.ShoppinCart, Tabs.Configuracion)

        tabs.forEach { tab ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 16.sp
                    )
                },
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}
