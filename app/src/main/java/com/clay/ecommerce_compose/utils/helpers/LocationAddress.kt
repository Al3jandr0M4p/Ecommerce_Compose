package com.clay.ecommerce_compose.utils.helpers

import android.location.Address

fun updateLocationTextFromAddress(address: Address): String {
    val street = address.thoroughfare ?: ""
    val city = address.locality ?: ""
    val subLocality = address.subLocality ?: ""

    return when {
        street.isNotEmpty() -> street
        subLocality.isNotEmpty() -> subLocality
        city.isNotEmpty() -> city
        else -> "Ubicacion no disponible"
    }
}
