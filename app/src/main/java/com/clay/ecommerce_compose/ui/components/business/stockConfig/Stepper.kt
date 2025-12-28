package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.auth.business.ImageInput
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountProductIntent
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stepper(
    sheetState: SheetState,
    onDimiss: () -> Unit,
    viewModel: BusinessAccountViewModel,
) {
    val state by viewModel.state.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showCategoryDropdown by remember { mutableStateOf(false) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDimiss,
        dragHandle = null,
        containerColor = colorResource(id = R.color.white)
    ) {
        // Header con botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { onDimiss() }) {
                Text(
                    text = "Cancelar",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.green)
                )
            }

            TextButton(onClick = {
                viewModel.handleIntent(BusinessAccountProductIntent.AddProduct)
            }) {
                Text(
                    text = "Guardar",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.green)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            // Nombre del producto
            BasicField(
                value = state.name,
                onValueChange = { name ->
                    viewModel.handleIntent(
                        intent = BusinessAccountProductIntent.ProductName(name = name.toString())
                    )
                },
                placeholder = "Nombre del producto",
                errorLabel = state.nameError
            )

            // Precio
            BasicField(
                value = state.price,
                onValueChange = { price ->
                    viewModel.handleIntent(
                        intent = BusinessAccountProductIntent.ProductPrice(price = price.toString())
                    )
                },
                placeholder = "Precio (0.00)",
                errorLabel = state.priceError
            )

            // Selector de Categoría
            if (categories.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = it }
                ) {
                    OutlinedTextField(
                        value = categories.find { it.id == state.categoryId }?.name
                            ?: "Sin categoría",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, "Seleccionar categoría")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin categoría") },
                            onClick = {
                                viewModel.handleIntent(BusinessAccountProductIntent.SetCategory(null))
                                showCategoryDropdown = false
                            }
                        )

                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.handleIntent(
                                        BusinessAccountProductIntent.SetCategory(category.id)
                                    )
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Control de Stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Controlar inventario",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = state.hasStockControl,
                    onCheckedChange = {
                        // Aquí podrías agregar un Intent para esto si lo necesitas
                    }
                )
            }

            // Campos de stock (solo si tiene control activado)
            if (state.hasStockControl) {
                BasicField(
                    value = state.stock,
                    onValueChange = { stock ->
                        viewModel.handleIntent(
                            intent = BusinessAccountProductIntent.ProductStock(stock = stock.toString())
                        )
                    },
                    placeholder = "Cantidad en stock",
                    errorLabel = state.stockError
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BasicField(
                        value = state.minStock.toString(),
                        onValueChange = { minStock ->
                            minStock?.toIntOrNull()?.let {
                                viewModel.handleIntent(
                                    intent = BusinessAccountProductIntent.SetMinStock(minStock = it)
                                )
                            }
                        },
                        placeholder = "Stock mínimo",
                    )

                    BasicField(
                        value = state.maxStock?.toString() ?: "",
                        onValueChange = { maxStock ->
                            viewModel.handleIntent(
                                intent = BusinessAccountProductIntent.SetMaxStock(
                                    maxStock = maxStock?.toIntOrNull()
                                )
                            )
                        },
                        placeholder = "Stock máximo (opcional)",
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Alertas de stock bajo",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Switch(
                        checked = state.stockAlertEnabled,
                        onCheckedChange = {
                            viewModel.handleIntent(
                                BusinessAccountProductIntent.SetStockAlertEnabled(it)
                            )
                        }
                    )
                }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Imagen del producto
            Text(
                "Imagen del producto",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            ImageInput { uri ->
                viewModel.handleIntent(
                    intent = BusinessAccountProductIntent.ProductImage(imgUrl = uri.toString())
                )
            }

            if (state.imgUrl.isNotBlank()) {
                Text(
                    text = "✓ Imagen seleccionada",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.green),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Descripción
            BasicField(
                value = state.description,
                onValueChange = { description ->
                    viewModel.handleIntent(
                        intent = BusinessAccountProductIntent.ProductDescription(
                            description = description.toString()
                        )
                    )
                },
                placeholder = "Descripción",
                minLines = 3
            )

            // Producto activo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Producto activo",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = state.isActive,
                    onCheckedChange = {
                        if (it) viewModel.handleIntent(BusinessAccountProductIntent.ProductActive)
                        else viewModel.handleIntent(BusinessAccountProductIntent.ProductInactive)
                    }
                )
            }
        }
    }
}