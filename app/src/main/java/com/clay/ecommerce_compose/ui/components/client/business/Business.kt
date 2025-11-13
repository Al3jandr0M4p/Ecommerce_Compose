package com.clay.ecommerce_compose.ui.components.client.business

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.Brand
import com.clay.ecommerce_compose.ui.components.client.sliders.SliderBranding
import com.clay.ecommerce_compose.ui.screens.client.home.HomeViewModel


@Composable
fun Business(
    navController: NavHostController,
    viewModel: HomeViewModel,
    brand: List<Brand>,
    pagerState: PagerState
) {
    val homeBusiness = viewModel.businessState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadBusiness()
    }

    val business = homeBusiness.value
//
//    val buss = getBusinesess()

    if (business.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No hay negocios disponibles",
                fontSize = 24.sp,
                style = MaterialTheme.typography.labelMedium
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Destacado en ${stringResource(id = R.string.app_name)}",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelMedium
                )

                IconButton(onClick = { /* TODO not yet implemented */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(size = 26.dp)
                    )
                }
            }

            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                items(items = business) { elements ->
                    Card(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(size = 10.dp))
                            .size(width = 290.dp, height = 220.dp)
                            .clickable {
                                navController.navigate(route = "details/${elements?.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.6.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                    ) {
                        AsyncImage(
                            model = elements?.logoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = 130.dp)
                                .clip(shape = RoundedCornerShape(size = 10.dp)),
                            onError = {
                                Log.e("BusinessImage", "Error al cargar la imagen")
                            }
                        )

                        Spacer(modifier = Modifier.height(height = 10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = elements?.name ?: "Unknown",
                                    fontSize = 16.sp,
                                    style = MaterialTheme.typography.labelMedium
                                )

                                IconButton(
                                    onClick = { /* TODO not yet implemented */ },
                                    modifier = Modifier.size(size = 20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = null,
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
                            ) {
                                Text(
                                    text = elements?.horarioApertura.toString(),
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = elements?.horarioCierre.toString(),
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(height = 16.dp))

        HorizontalDivider(
            modifier = Modifier
                .height(height = 1.4.dp)
                .padding(vertical = 6.dp),
            thickness = DividerDefaults.Thickness,
            color = colorResource(id = R.color.lightGrey)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Lugares que te pueden gustar",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelMedium
                )

                IconButton(onClick = { /* TODO not yet implemented */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(size = 26.dp)
                    )
                }
            }

            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                items(items = business) { elements ->
                    Card(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(size = 10.dp))
                            .size(width = 290.dp, height = 220.dp)
                            .clickable {
                                Log.d("IdBusiness", "id ${elements?.id}")
                                navController.navigate(route = "details/${elements?.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.6.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                    ) {
                        AsyncImage(
                            model = elements?.logoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = 130.dp)
                                .clip(shape = RoundedCornerShape(size = 10.dp)),
                            onError = {
                                Log.e("BusinessImage", "Error al cargar la imagen")
                            }
                        )

                        Spacer(modifier = Modifier.height(height = 10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = elements?.name.toString(),
                                    fontSize = 16.sp,
                                    style = MaterialTheme.typography.labelMedium
                                )

                                IconButton(
                                    onClick = { /* TODO not yet implemented */ },
                                    modifier = Modifier.size(size = 20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = null,
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
                            ) {
                                Text(
                                    text = elements?.horarioApertura.toString(),
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = elements?.horarioCierre.toString(),
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(height = 16.dp))

        HorizontalDivider(
            modifier = Modifier
                .height(height = 1.4.dp)
                .padding(vertical = 6.dp),
            thickness = DividerDefaults.Thickness,
            color = colorResource(id = R.color.lightGrey)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Negocios cercanos",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelMedium
                )

                IconButton(onClick = { /* TODO not yet implemented */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(size = 26.dp)
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(items = business) { elements ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.width(width = 70.dp)
                    ) {
                        AsyncImage(
                            model = elements?.logoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(size = 68.dp)
                                .clip(shape = CircleShape),
                            onError = {
                                Log.e("BusinessImage", "Error al cargar la imagen")
                            }
                        )

                        Spacer(modifier = Modifier.height(height = 4.dp))

                        Text(
                            text = elements?.name.toString(),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Text(
                            text = elements?.horarioApertura.toString(),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Text(
                            text = elements?.horarioCierre.toString(),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .height(height = 1.4.dp)
                .padding(vertical = 6.dp),
            thickness = DividerDefaults.Thickness,
            color = colorResource(id = R.color.lightGrey)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                beyondViewportPageCount = 1,
                modifier = Modifier
                    .height(height = 180.dp),
            ) { page ->
                val bran = brand[page]
                SliderBranding(
                    title = bran.title,
                    color = bran.color,
                    description = bran.description,
                    logo = bran.logo
                )
            }
        }
    }
}
