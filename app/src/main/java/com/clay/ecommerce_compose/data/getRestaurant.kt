package com.clay.ecommerce_compose.data

import androidx.compose.runtime.Immutable
import com.clay.ecommerce_compose.R

@Immutable
data class Products(
    val id: Int,
    val title: String,
    val imgUrl: Int,
    val description: String?
)

@Immutable
data class Restaurant(
    val id: Int,
    val title: String,
    val imgUrl: Int,
    val description: String?,
    val products: List<Products>
)

fun getRestaurants(): List<Restaurant> = listOf(
    Restaurant(
        id = 1,
        title = "Bonifacio 1",
        imgUrl = R.drawable.ic_launcher_background,
        description = "esta es la descripcion mas larga que yo e visto y e dado en mi vida entera en una app",
        products = listOf(
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            ),
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            ),
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            ),
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            )
        )
    ),
    Restaurant(
        id = 2,
        title = "Bonifacio 2",
        imgUrl = R.drawable.ic_launcher_background,
        description = "esta es la descripcion mas larga que yo e visto y e dado en mi vida entera en una app",
        products = listOf(
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            ),
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            ),
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            )
        )
    ),
    Restaurant(
        id = 3,
        title = "Bonifacio 3",
        imgUrl = R.drawable.ic_launcher_background,
        description = "esta es la descripcion mas larga que yo e visto y e dado en mi vida entera en una app",
        products = listOf(
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            ),
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            )
        )
    ),
    Restaurant(
        id = 4,
        title = "Bonifacio 4",
        imgUrl = R.drawable.ic_launcher_background,
        description = "esta es la descripcion mas larga que yo e visto y e dado en mi vida entera en una app",
        products = listOf(
            Products(
                id = 1,
                title = "hambre 1",
                imgUrl = R.drawable.ic_launcher_background,
                description = "Dios mio que descripcion mas larga"
            )
        )
    )
)