package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.clay.ecommerce_compose.domain.model.StockMovement
import com.clay.ecommerce_compose.domain.model.StockMovementType
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@Composable
fun RestockDialog(
    productId: Int,
    productName: String,
    currentStock: Int?,
    viewModel: BusinessAccountViewModel,
    onDismiss: () -> Unit
) {
    val safeCurrentStock = currentStock ?: 0
    var quantity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedMovementType by remember { mutableStateOf(StockMovementType.RESTOCK) }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = "Ajustar Stock",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Nombre del producto
                Text(
                    text = productName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Stock actual
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stock actual:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = safeCurrentStock.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Tipo de movimiento
                Text(
                    text = "Tipo de operación",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedMovementType == StockMovementType.RESTOCK,
                        onClick = { selectedMovementType = StockMovementType.RESTOCK },
                        label = { Text("Reabastecer") },
                        leadingIcon = if (selectedMovementType == StockMovementType.RESTOCK) {
                            { Icon(Icons.Default.Add, "Agregar") }
                        } else null,
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = selectedMovementType == StockMovementType.ADJUSTMENT,
                        onClick = { selectedMovementType = StockMovementType.ADJUSTMENT },
                        label = { Text("Ajuste") },
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = selectedMovementType == StockMovementType.RETURN,
                        onClick = { selectedMovementType = StockMovementType.RETURN },
                        label = { Text("Devolución") },
                        leadingIcon = if (selectedMovementType == StockMovementType.RETURN) {
                            { Icon(Icons.Default.Add, "Devolver") }
                        } else null,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Campo de cantidad
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it
                        showError = false
                    },
                    label = {
                        Text(
                            when (selectedMovementType) {
                                StockMovementType.RESTOCK -> "Cantidad a agregar"
                                StockMovementType.ADJUSTMENT -> "Nuevo stock total"
                                StockMovementType.RETURN -> "Cantidad devuelta"
                                else -> "Cantidad"
                            }
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = showError,
                    supportingText = if (showError) {
                        { Text("Ingrese una cantidad válida") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )

                // Vista previa del nuevo stock
                if (quantity.toIntOrNull() != null) {
                    val newStock = when (selectedMovementType) {
                        StockMovementType.RESTOCK -> safeCurrentStock + quantity.toInt()
                        StockMovementType.ADJUSTMENT -> quantity.toInt()
                        StockMovementType.RETURN -> safeCurrentStock + quantity.toInt()
                        else -> safeCurrentStock
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (newStock >= 0)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nuevo stock:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = newStock.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Notas
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas (opcional)") },
                    placeholder = { Text("Ej: Compra a proveedor X") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val quantityInt = quantity.toIntOrNull()
                            if (quantityInt == null || quantityInt <= 0) {
                                showError = true
                                return@Button
                            }

                            viewModel.adjustStock(
                                productId = productId,
                                quantity = quantityInt,
                                movementType = selectedMovementType,
                                notes = notes.ifBlank { null }
                            )
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirmar")
                    }
                }
            }
        }
    }
}

@Composable
fun StockMovementHistoryDialog(
    productName: String,
    movements: List<StockMovement>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Historial de Movimientos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = productName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (movements.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay movimientos registrados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        movements.forEach { movement ->
                            StockMovementItem(movement)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockMovementItem(movement: StockMovement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (movement.movementType) {
                StockMovementType.SALE -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                StockMovementType.RESTOCK, StockMovementType.RETURN ->
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                StockMovementType.ADJUSTMENT -> MaterialTheme.colorScheme.secondaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (movement.movementType) {
                        StockMovementType.SALE -> "Venta"
                        StockMovementType.RESTOCK -> "Reabastecimiento"
                        StockMovementType.ADJUSTMENT -> "Ajuste"
                        StockMovementType.RETURN -> "Devolución"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${movement.previousStock} → ${movement.newStock}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cantidad: ${if (movement.movementType == StockMovementType.SALE) "-" else "+"}${movement.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = movement.createdAt ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            movement.notes?.let { notes ->
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
