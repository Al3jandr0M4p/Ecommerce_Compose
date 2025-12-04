package com.clay.ecommerce_compose.ui.components.admin.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.domain.model.Business
import com.clay.ecommerce_compose.utils.hooks.useEditDialog

@Composable
fun EditBusinessDialog(
    business: Business,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    val (name,
        rnc,
        address,
        isRncValid,
        isFormValid,
        onNameChange,
        onRncChange,
        onAddressChange) = useEditDialog(business)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Negocio", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(space = 12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(text = "Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = rnc,
                    onValueChange = onRncChange,
                    label = { Text(text = "RNC") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isRncValid && rnc.isNotEmpty(),
                    supportingText = {
                        if (!isRncValid && rnc.isNotEmpty()) {
                            Text(text = "El RNC debe tener 13 dígitos.")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = onAddressChange,
                    label = { Text(text = "Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name, rnc, address) },
                enabled = isFormValid
            ) {
                Text(text = "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}