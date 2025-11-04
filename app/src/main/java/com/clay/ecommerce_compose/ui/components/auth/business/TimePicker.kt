package com.clay.ecommerce_compose.ui.components.auth.business

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerComponent(label: String, onTimeSelected: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(value = false) }

    var selectedTimeDisplay by remember { mutableStateOf(value = "") }

    TextButton(onClick = { showDialog = true }) {
        Text(text = selectedTimeDisplay.ifEmpty { label })
    }

    if (showDialog) {
        val timePickerState = rememberTimePickerState(is24Hour = false)

        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        val dbTimeFormat = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

                        selectedTimeDisplay = time.format(DateTimeFormatter.ofPattern("hh:mm a"))

                        onTimeSelected(dbTimeFormat)

                        showDialog = false
                    }
                ) { Text(text = "Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancelar")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
