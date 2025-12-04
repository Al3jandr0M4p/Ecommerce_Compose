package com.clay.ecommerce_compose.utils.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.clay.ecommerce_compose.domain.model.Business

data class EditBusinessDialogState(
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
fun useEditDialog(business: Business): EditBusinessDialogState {

    var name by remember { mutableStateOf(value = business.name) }
    var rnc by remember { mutableStateOf(value = business.rnc) }
    var address by remember { mutableStateOf(value = business.address) }

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

    return EditBusinessDialogState(
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