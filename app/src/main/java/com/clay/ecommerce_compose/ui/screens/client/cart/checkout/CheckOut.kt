package com.clay.ecommerce_compose.ui.screens.client.cart.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.cart.CartStepper
import com.clay.ecommerce_compose.ui.components.client.cart.PaymentSummary
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(cartViewModel: CartViewModel, navController: NavHostController) {
    val state by cartViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.white))
                    .padding(horizontal = 6.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Finalizar Compra",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }, navigationIcon = {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.Companion.clickable {
                                navController.navigate(route = "cart")
                            },
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                )

                CartStepper(
                    steps = listOf("Carrito", "Checkout", "Delivery"),
                    currentStep = 1
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
//                        navController.navigate(route = "")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.black),
                        contentColor = colorResource(id = R.color.white)
                    )
                ) {
                    Text(
                        text = "Continuar",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorResource(id = R.color.white)
                    )
                }
                Spacer(modifier = Modifier.height(height = 12.dp))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white))
        ) {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Elige un metodo\nde pago",
                        fontSize = 55.sp,
                        lineHeight = 70.sp,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        text = "No se realizara ningun cargo hasta que no se\ntermine la orden en la siguiente review",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.labelSmall,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                HorizontalDivider(
                    thickness = 2.5.dp,
                    color = colorResource(id = R.color.lightGrey)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Pagos
                }
            }

            item {
                PaymentSummary(
                    subtotal = state.subTotal,
                    deliveryFee = state.deliveryFee,
                    serviceFee = state.serviceFee,
                    itbis = state.subTotal * 0.18,
                    total = state.totalPrice
                )
            }

            item {
                // Cupones
            }

        }
    }
}
