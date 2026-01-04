package com.clay.ecommerce_compose.utils.helpers

import java.text.NumberFormat
import java.util.Locale

private val DOMINICAN_LOCALE: Locale = Locale.Builder()
    .setLanguage("es")
    .setRegion("DO")
    .build()

fun formatPrice(amount: Double): String {
    val format = NumberFormat.getNumberInstance(DOMINICAN_LOCALE)
    format.maximumFractionDigits = 0
    return "$${format.format(amount)}"
}
