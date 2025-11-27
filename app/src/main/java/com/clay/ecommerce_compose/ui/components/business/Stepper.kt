package com.clay.ecommerce_compose.ui.components.business

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDimiss,
        dragHandle = null,
        containerColor = colorResource(id = R.color.white)
    ) {
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

            TextButton(onClick = { viewModel.handleIntent(BusinessAccountProductIntent.AddProduct) }) {
                Text(
                    text = "Guardar",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.green)
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            BasicField(
                value = state.name,
                onValueChange = { name ->
                    viewModel.handleIntent(
                        intent = BusinessAccountProductIntent.ProductName(name = name.toString())
                    )
                },
                placeholder = "Nombre del producto",
            )
            BasicField(
                value = state.price, onValueChange = { price ->
                    viewModel.handleIntent(
                        intent = BusinessAccountProductIntent.ProductPrice(price = price.toString())
                    )
                }, placeholder = "Precio (0.00)"
            )

            Spacer(modifier = Modifier.padding(top = 10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Controlar stock",
                    style = MaterialTheme.typography.bodyMedium
                )

                Switch(
                    checked = state.hasStockControl,
                    onCheckedChange = { has ->
                        viewModel.handleIntent(
                            intent = BusinessAccountProductIntent.SetProductStockControl(has = has)
                        )
                    }
                )
            }

            if (state.hasStockControl) {
                BasicField(
                    value = state.stock,
                    onValueChange = { stock ->
                        viewModel.handleIntent(
                            intent = BusinessAccountProductIntent.ProductStock(stock = stock.toString())
                        )
                    },
                    placeholder = "Cantidad en stock",
                )
            }

            Spacer(Modifier.padding(top = 8.dp))

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
                    text = "Imagen seleccionada",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.green),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.padding(top = 12.dp))

            BasicField(
                value = state.description,
                onValueChange = { description ->
                    viewModel.handleIntent(
                        intent = BusinessAccountProductIntent.ProductDescription(description = description.toString())
                    )
                },
                placeholder = "Descripción",
            )

            Spacer(Modifier.padding(top = 10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
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
//        BasicField(
//            value = state?.imgUrl,
//            onValueChange = {
//                viewModel.handleIntent(
//                    BusinessAccountProductIntent.ProductImage(state?.imgUrl)
//                )
//            }, placeholder = "Imagen"
//        )
        }
    }
}
