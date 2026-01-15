package com.clay.ecommerce_compose.utils.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatTime(timestamp: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = parser.parse(timestamp) ?: return ""

        val now = Calendar.getInstance()
        val messageDate = Calendar.getInstance().apply { time = date }

        val formatter = when {
            now.get(Calendar.DAY_OF_YEAR) == messageDate.get(Calendar.DAY_OF_YEAR) &&
                    now.get(Calendar.YEAR) == messageDate.get(Calendar.YEAR) -> {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            }

            now.get(Calendar.WEEK_OF_YEAR) == messageDate.get(Calendar.WEEK_OF_YEAR) &&
                    now.get(Calendar.YEAR) == messageDate.get(Calendar.YEAR) -> {
                SimpleDateFormat("EEE HH:mm", Locale.getDefault())
            }

            else -> {
                SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            }
        }
        formatter.format(date)
    } catch (e: Exception) {
        ""
    }
}