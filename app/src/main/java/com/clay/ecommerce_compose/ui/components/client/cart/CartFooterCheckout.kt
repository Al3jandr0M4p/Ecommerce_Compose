package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.screens.client.cart.CartState
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun CartFooterCheckOut(
    cartState: CartState,
    navController: NavHostController
) {
    val itbisAmount = cartState.subTotal * 0.18
    val finalTotal = cartState.subTotal + itbisAmount + cartState.serviceFee + cartState.deliveryFee

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            )
            .clip(
                shape = RoundedCornerShape(
                    topEnd = 14.dp,
                    topStart = 14.dp
                )
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {

            PriceRow(
                label = "Subtotal",
                value = cartState.subTotal
            )

            PriceRow(
                label = "ITBIS (18%)",
                value = itbisAmount
            )

            if (cartState.serviceFee > 0) {
                PriceRow(
                    label = "Cargo de servicio",
                    value = cartState.serviceFee
                )
            }

            if (cartState.deliveryFee > 0) {
                PriceRow(
                    label = "Delivery",
                    value = cartState.deliveryFee
                )
            }

            PriceRow(
                label = "Total",
                value = finalTotal,
                isTotal = true
            )

            Button(
                onClick = {
                    navController.navigate(route = "checkout/all")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.black),
                    contentColor = colorResource(id = R.color.white)
                )
            ) {
                Text(
                    text = "Finalizar Compra ${formatPrice(finalTotal)}",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelMedium,
                    color = colorResource(id = R.color.white)
                )
            }

            Spacer(modifier = Modifier.height(height = 18.dp))
        }
    }
}
