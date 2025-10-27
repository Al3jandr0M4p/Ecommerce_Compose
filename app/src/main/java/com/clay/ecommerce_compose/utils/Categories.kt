package com.clay.ecommerce_compose.utils

import androidx.compose.runtime.Immutable
import com.clay.ecommerce_compose.R

@Immutable
data class Category(
    val title: String,
    val img: Int
)

fun getCategoriesList(): List<Category> {
    return listOf(
        Category(title = "Colmados", img = R.drawable.ic_launcher_background),
        Category(title = "Panaderías", img = R.drawable.ic_launcher_background),
        Category(title = "Frutas y Verduras", img = R.drawable.ic_launcher_background),
        Category(title = "Comida Rápida", img = R.drawable.ic_pizza),
        Category(title = "Restaurantes", img = R.drawable.ic_launcher_background),
        Category(title = "Limpieza y Hogar", img = R.drawable.ic_launcher_background),
        Category(title = "Electronicos", img = R.drawable.ic_monitor_50),
    )
}

