package com.clay.ecommerce_compose.ui.components.client.cart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.screens.client.cart.CartIntent
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartsScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    val cartState by cartViewModel.state.collectAsState()

    val cartItems = cartState.items
    val cartItemByBusiness = cartItems.groupBy { it.businessId }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(color = colorResource(id = R.color.white))) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Carrito",
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }, navigationIcon = {
                        IconButton(onClick = { navController.navigate(route = "userHome") }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                )

                CartStepper(
                    steps = listOf("Carrito", "Checkout", "Delivery"),
                    currentStep = 0
                )
            }
        },
        bottomBar = {
            CartFooterCheckOut(
                cartState = cartState,
                navController = navController
            )
        },
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(color = colorResource(id = R.color.white))
        ) {
            items(items = cartItemByBusiness.toList()) { (_, items) ->
                Card(
                    modifier = Modifier.padding(vertical = 10.dp),
                    shape = RoundedCornerShape(size = 0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AsyncImage(
                            model = items.first().businessImg,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 55.dp, height = 45.dp)
                                .clip(shape = RoundedCornerShape(size = 16.dp))
                        )
                        Text(
                            text = items.first().businessName,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    items.forEach { item ->
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(all = 10.dp)
                                    .size(width = 100.dp, height = 70.dp)
                                    .clip(shape = RoundedCornerShape(size = 16.dp)),
                                onError = {
                                    Log.e("AsyncImageCart", it.result.throwable.message ?: "")
                                })
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = item.name)
                                    Text(text = formatPrice(amount = item.price * item.quantity))
                                }

                                Spacer(modifier = Modifier.height(height = 10.dp))

                                QuantitySelector(
                                    quantity = item.quantity,
                                    stock = item.stock,

                                    onIncrease = {
                                        cartViewModel.handleIntent(
                                            intent = CartIntent.UpdateQuantity(
                                                itemId = item.id, quantity = item.quantity + 1
                                            )
                                        )
                                    },

                                    onDecreaseOrDelete = {
                                        if (item.quantity == 1) {
                                            cartViewModel.handleIntent(
                                                intent = CartIntent.RemoveItem(itemId = item.id)
                                            )
                                        } else {
                                            cartViewModel.handleIntent(
                                                intent = CartIntent.UpdateQuantity(
                                                    itemId = item.id, quantity = item.quantity - 1
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    thickness = 3.5.dp,
                    color = colorResource(id = R.color.lightGrey)
                )
            }
        }
    }
}
