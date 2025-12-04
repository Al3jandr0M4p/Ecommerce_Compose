package com.clay.ecommerce_compose.ui.components.admin.tabs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BusinessTabs(
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    pendingCount: Int
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTab,
        Modifier,
        containerColor = Color.White,
        contentColor = Color(color = 0xFF3498DB),
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(selectedTabIndex = selectedTab)
            )
        },
        divider = { HorizontalDivider() },
    ) {
        Tab(
            selected = selectedTab == 0,
            onClick = { onTabChange(0) },
            text = { Text(text = "Todos") }
        )
        Tab(
            selected = selectedTab == 1,
            onClick = { onTabChange(1) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Pendientes")
                    if (pendingCount > 0) {
                        Spacer(modifier = Modifier.width(width = 6.dp))
                        Badge(containerColor = Color(color = 0xFFF39C12)) {
                            Text(text = "$pendingCount", fontSize = 10.sp)
                        }
                    }
                }
            }
        )
    }
}
