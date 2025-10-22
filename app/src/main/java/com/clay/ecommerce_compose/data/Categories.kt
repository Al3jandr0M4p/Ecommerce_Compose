package com.clay.ecommerce_compose.data

import androidx.compose.runtime.Immutable
import com.clay.ecommerce_compose.R


@Immutable
data class Categories(
    val title: String,
    val img: Int
)

fun getCategoriesList(): List<Categories> {
    return listOf(
        Categories(title = "Productos del\nSuper", img = R.drawable.ic_shopping_cart),
        Categories(title = "Alcoho\nlicos", img = R.drawable.ic_beer_botle),
        Categories(title = "Fast Food", img = R.drawable.ic_pizza),
        Categories(title = "Basicos", img = R.drawable.ic_shopping_bag),
        Categories(title = "Electronicos", img = R.drawable.ic_monitor_50),
        // agregar mas categorias segun sea necesario
    )
}

fun getCategory(): List<Categories> {
    return listOf(
        Categories(title = "Fast Food", img = R.drawable.ic_pizza),
        Categories(title = "Basicos", img = R.drawable.ic_shopping_bag),
        Categories(title = "Electronicos", img = R.drawable.ic_monitor_50),
    )
}