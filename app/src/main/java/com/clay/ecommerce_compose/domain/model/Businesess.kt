package com.clay.ecommerce_compose.domain.model

import androidx.compose.runtime.Immutable
import com.clay.ecommerce_compose.R


@Immutable
data class Businesess(
    val id: Int,
    val img: Int,
    val name: String,
    val horario: String,
    val time: String
)

fun getBusinesess() = listOf(
    Businesess(
        id = 1,
        img = R.drawable.ic_launcher_background,
        name = "Megacentro",
        horario = "8:00 AM",
        time = "10 min"
    ),
    Businesess(
        id = 2,
        img = R.drawable.ic_launcher_background,
        name = "Plaza Central",
        horario = "8:00 AM",
        time = "10 min"
    ),
    Businesess(
        id = 3,
        img = R.drawable.ic_launcher_background,
        name = "Sambil",
        horario = "8:00 AM",
        time = "10 min"
    ),
    Businesess(
        id = 4,
        img = R.drawable.ic_launcher_background,
        name = "Galeria 360",
        horario = "8:00 AM",
        time = "10 min"
    ),
    Businesess(
        id = 5,
        img = R.drawable.ic_launcher_background,
        name = "La Sirena",
        horario = "8:00 AM",
        time = "10 min"
    ),
    Businesess(
        id = 6,
        img = R.drawable.ic_launcher_background,
        name = "Lasalle",
        horario = "8:00 AM",
        time = "10 min"
    )
)
