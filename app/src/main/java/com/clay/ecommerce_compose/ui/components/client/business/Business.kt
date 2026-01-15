package com.clay.ecommerce_compose.ui.components.client.business

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.screens.client.home.FavoriteViewModel
import com.clay.ecommerce_compose.ui.screens.client.home.HomeViewModel


@Composable
fun Business(
    navController: NavHostController,
    viewModel: HomeViewModel,
    favoritesViewModel: FavoriteViewModel,
    isTablet: Boolean,
) {
    val business by viewModel.businessState.collectAsState()
    val favorites by favoritesViewModel.favorites.collectAsState()

    Box(
        modifier = Modifier
    ) {
        when {
            business.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay negocios disponibles",
                    fontSize = if (isTablet) 44.sp else 30.sp,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> Column(
                modifier = Modifier
                    .padding(horizontal = if (isTablet) 20.dp else 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = if (isTablet) 20.dp else 16.dp)
            ) {
                business.forEach { elements ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = if (isTablet) 20.dp else 18.dp)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(size = if (isTablet) 14.dp else 10.dp))
                            .height(height = if (isTablet) 320.dp else 220.dp)
                            .clickable {
                                navController.navigate(route = "details/${elements?.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isTablet) 1.dp else 0.8.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    ) {
                        AsyncImage(
                            model = elements?.logoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = if (isTablet) 210.dp else 145.dp)
                                .clip(shape = RoundedCornerShape(size = 10.dp)),
                            onError = {
                                Log.e("BusinessImage", "Error al cargar la imagen")
                            },
                            filterQuality = FilterQuality.Low
                        )

                        Spacer(modifier = Modifier.height(height = if (isTablet) 14.dp else 10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = if (isTablet) 10.dp else 8.dp,
                                    vertical = if (isTablet) 8.dp else 4.dp
                                )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = elements?.name ?: "Unknown",
                                    fontSize = if (isTablet) 20.sp else 16.sp,
                                    style = MaterialTheme.typography.labelMedium
                                )

                                IconButton(
                                    onClick = {
                                        elements?.id.let {
                                            favoritesViewModel.toggleFavorite(
                                                businessId = it.toString()
                                            )
                                        }
                                    },
                                    modifier = Modifier.size(size = if (isTablet) 30.dp else 20.dp)
                                ) {
                                    val isFavorite =
                                        elements?.id?.let {
                                            favorites.contains(it.toString())
                                        } ?: false

                                    Icon(
                                        imageVector = if (isFavorite)
                                            Icons.Filled.Favorite else
                                            Icons.Outlined.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (isFavorite)
                                            colorResource(id = R.color.tintRed) else
                                            colorResource(id = R.color.black)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = if (isTablet) 14.dp else 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(space = if (isTablet) 8.dp else 4.dp)
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
    }
}
