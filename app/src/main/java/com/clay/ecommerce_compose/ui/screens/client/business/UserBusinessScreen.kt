package com.clay.ecommerce_compose.ui.screens.client.business

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.getBusinesess
import com.clay.ecommerce_compose.ui.components.bars.MyTopAppBar
import com.clay.ecommerce_compose.ui.components.client.business.ProductCard
import com.clay.ecommerce_compose.ui.components.client.business.SettingsButtonIcons
import com.clay.ecommerce_compose.ui.screens.client.cart.CartItem
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserBusinessScreen(
    navController: NavHostController,
    idBusiness: Int?,
    cartViewModel: CartViewModel,
) {
    val buss = getBusinesess().find { it.id == idBusiness }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberLazyListState()

    val isAtTop =
        scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset < 10

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            MyTopAppBar(
                navController = navController,
                isAtTop = isAtTop,
                scrollBehavior = scrollBehavior,
                idShop = buss?.id
            )
        }
    ) { innerPadding ->
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
                        .height(height = 180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 10.dp, top = 8.dp)
                    ) {
                        SettingsButtonIcons(
                            icon = Icons.Outlined.Search,
                            clickable = { navController.navigate(route = "searchInShop/${buss?.id}") })

                        Spacer(modifier = Modifier.width(width = 5.dp))

                        SettingsButtonIcons(icon = Icons.Outlined.FavoriteBorder)

                        Spacer(modifier = Modifier.width(width = 5.dp))

                        SettingsButtonIcons(icon = Icons.Outlined.MoreHoriz)
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ic_nike_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = 150.dp)
                            .height(height = 70.dp)
                            .clip(shape = CircleShape)
                            .shadow(elevation = 6.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(height = 38.dp))
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nombre del negocio",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelSmall
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
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(18) {
                            ProductCard(
                                title = "Un buen producto",
                                price = "${20 + (it * 100)}",
                                image = R.drawable.ic_launcher_background,
                                modifier = Modifier.weight(weight = 1f),
                                onAddClick = {
                                    cartViewModel.handleIntent(CartItem("1", "Pan", 2.5, "", 1))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
