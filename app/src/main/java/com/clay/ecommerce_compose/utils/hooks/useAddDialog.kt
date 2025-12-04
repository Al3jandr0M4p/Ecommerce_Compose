package com.clay.ecommerce_compose.utils.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

data class AddBusinessDialogState(
    val name: String,
    val rnc: String,
    val address: String,
    val isRncValid: Boolean,
    val isFormValid: Boolean,
    val onNameChange: (String) -> Unit,
    val onRncChange: (String) -> Unit,
    val onAddressChange: (String) -> Unit
)


@Composable
fun useAddBusinessDialog(): AddBusinessDialogState {

    var name by remember { mutableStateOf("") }
    var rnc by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val isRncValid = rnc.length == 13
    val isFormValid = name.isNotBlank() && isRncValid && address.isNotBlank()

    fun onNameChange(new: String) {
        name = new
    }

    fun onRncChange(new: String) {
        if (new.length <= 13 && new.all { it.isDigit() }) {
            rnc = new
        }
    }

    fun onAddressChange(new: String) {
        address = new
    }

    return AddBusinessDialogState(
        name = name,
        rnc = rnc,
        address = address,
        isRncValid = isRncValid,
        isFormValid = isFormValid,
        onNameChange = ::onNameChange,
        onRncChange = ::onRncChange,
        onAddressChange = ::onAddressChange
    )
}
