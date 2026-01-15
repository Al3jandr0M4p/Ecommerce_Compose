package com.clay.ecommerce_compose.ui.components.client.business

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.ProductPayload
import com.clay.ecommerce_compose.ui.screens.client.cart.CartIntent
import com.clay.ecommerce_compose.ui.screens.client.cart.CartItem
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun UserBusinessComponent(
    innerPadding: PaddingValues,
    scrollState: LazyListState,
    buss: BusinessProfile?,
    products: List<ProductPayload>,
    navController: NavHostController,
    cartViewModel: CartViewModel,
) {
    var expanded by remember { mutableStateOf(value = false) }
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val windowSize = calculateWindowSizeClass(activity)
    val isTablet = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .padding(paddingValues = innerPadding)
            .fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = if (isTablet) 480.dp else 300.dp)
            ) {
                AsyncImage(
                    model = buss?.logoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = if (isTablet) 480.dp else 300.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopEnd)
                        .padding(start = 10.dp, end = 10.dp, top = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SettingsButtonIcons(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        clickable = {
                            navController.navigate(route = "userHome")
                        },
                        modifier = Modifier.size(size = 40.dp)
                    )

                    Row {
                        SettingsButtonIcons(
                            icon = Icons.Outlined.Search, clickable = {
                                navController.navigate(route = "searchInShop/${buss?.id}") {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }, modifier = Modifier.size(size = 40.dp)
                        )
                        Spacer(modifier = Modifier.width(width = 5.dp))
                        SettingsButtonIcons(
                            icon = Icons.Outlined.FavoriteBorder,
                            modifier = Modifier.size(size = 40.dp)
                        )
                        Spacer(modifier = Modifier.width(width = 5.dp))
                        SettingsButtonIcons(
                            icon = Icons.Outlined.MoreHoriz,
                            modifier = Modifier.size(size = 40.dp),
                            clickable = { expanded = true }
                        )

                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(
                                onClick = {
                                    val url =
                                        "https://wa.me/+1${buss?.phone}?text=${Uri.encode("hola")}"
                                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                    context.startActivity(intent)
                                    expanded = false
                                },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_whatsapp),
                                        contentDescription = null,
                                        modifier = Modifier.size(size = 22.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Contactar por whatsapp",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 14.sp
                                    )
                                }
                            )

                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate(route = "activity/business/${buss!!.id}")
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Message,
                                        contentDescription = null,
                                        modifier = Modifier.size(size = 20.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Mensaje Interno",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 14.sp
                                    )
                                }
                            )

                        }
                    }
                }
            }
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buss?.name.toString(),
                    fontSize = if (isTablet) 30.sp else 20.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(
                        top = if (isTablet) 12.dp else 8.dp,
                        bottom = if (isTablet) 8.dp else 4.dp
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "4.7",
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = null,
                            modifier = Modifier.size(size = 12.dp)
                        )
                    }

                    Text(
                        text = "(2000+)",
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorResource(id = R.color.coolGrey)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(space = 2.dp)
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.app_name)} One",
                            color = colorResource(id = R.color.gold),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier.size(size = 10.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(height = 30.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Productos destacados",
                    fontSize = if (isTablet) 28.sp else 20.sp,
                    style = if (isTablet) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = if (isTablet) 10.dp else 8.dp)
                )

                if (isTablet) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(items = products) { product ->
                            val isOutOfStock = product.stock <= 0
                            ProductCard(
                                title = product.name,
                                price = product.price,
                                image = product.imageUrl,
                                onAddClick = {
                                    if (!isOutOfStock) {
                                        cartViewModel.handleIntent(
                                            intent = CartIntent.AddItem(
                                                item = CartItem(
                                                    id = product.id,
                                                    businessName = buss?.name.toString(),
                                                    businessImg = buss?.logoUrl.toString(),
                                                    businessId = buss!!.id,
                                                    name = product.name,
                                                    price = product.price,
                                                    imageUrl = product.imageUrl,
                                                    quantity = 1,
                                                    stock = product.stock
                                                )
                                            )
                                        )
                                    }
                                    Log.d("AddProduct", "Click add product: ${product.id}")
                                },
                                enabled = !isOutOfStock
                            )
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        products.forEach { product ->
                            val isOutOfStock = product.stock <= 0
                            ProductCard(
                                title = product.name,
                                price = product.price,
                                image = product.imageUrl,
                                onAddClick = {
                                    if (!isOutOfStock) {
                                        cartViewModel.handleIntent(
                                            intent = CartIntent.AddItem(
                                                item = CartItem(
                                                    id = product.id,
                                                    businessName = buss?.name.toString(),
                                                    businessImg = buss?.logoUrl.toString(),
                                                    businessId = buss!!.id,
                                                    name = product.name,
                                                    price = product.price,
                                                    imageUrl = product.imageUrl,
                                                    quantity = 1,
                                                    stock = product.stock
                                                )
                                            )
                                        )
                                    }
                                    Log.d("AddProduct", "Click add product: ${product.id}")
                                },
                                enabled = !isOutOfStock
                            )
                            Spacer(modifier = Modifier.height(height = 30.dp))
                        }
                    }
                }
            }
        }
    }
}
