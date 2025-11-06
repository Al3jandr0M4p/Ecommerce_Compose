package com.clay.ecommerce_compose.ui.components.client.business

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.getBusinesess


@Composable
fun Business(navController: NavHostController) {
    val buss = getBusinesess()

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
            items(items = buss) { elements ->
                Card(
                    modifier = Modifier
                        .size(width = 290.dp, height = 220.dp)
                        .clickable {
                            navController.navigate(route = "details/${elements.id}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.6.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                ) {
                    Image(
                        painter = painterResource(id = elements.img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 130.dp)
                            .clip(shape = RoundedCornerShape(size = 10.dp))
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
                                text = elements.name,
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

                        Text(
                            text = elements.time,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
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
            items(items = buss) { elements ->
                Card(
                    modifier = Modifier
                        .size(width = 290.dp, height = 220.dp)
                        .clickable {
                            navController.navigate(route = "details/${elements.id}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.6.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                ) {
                    Image(
                        painter = painterResource(id = elements.img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 130.dp)
                            .clip(shape = RoundedCornerShape(size = 10.dp))
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
                                text = elements.name,
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

                        Text(
                            text = elements.time,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
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
            items(items = buss) { element ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(width = 70.dp)
                ) {
                    Image(
                        painter = painterResource(id = element.img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(size = 68.dp)
                            .clip(shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.height(height = 4.dp))

                    Text(
                        text = element.name,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = element.horario,
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.labelSmall
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
}
