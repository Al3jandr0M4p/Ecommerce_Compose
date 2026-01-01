package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Store
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductAttributesRow(stock: Int, categoryId: Int?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
    ) {
        ProductAttributeItem(Icons.Outlined.Inventory2, label = "$stock Stock")
        if (categoryId != null) {
            ProductAttributeItem(Icons.Outlined.Category, label = "Categoria $categoryId")
        }
        ProductAttributeItem(Icons.Outlined.Store, label = "Negocio")
    }
}
