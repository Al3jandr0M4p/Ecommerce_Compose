package com.clay.ecommerce_compose.ui.components.business.stockConfig

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.business.SettingsButtonIcons
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ProductDetails(
    id: Int?,
    name: String,
    price: Double,
    imageUrl: String?,
    stock: Int,
    description: String?,
    categoryId: Int?,
    businessId: Int,
    navController: NavHostController
) {

    val systemUIController = rememberSystemUiController()

    SideEffect {
        systemUIController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(
            left = 0,
            top = 0,
            right = 0,
            bottom = 100,
        ),

        bottomBar = {
            BottomAppBar {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 100.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Button(
                        onClick = {
                            /*TODO*/
                        },
                        modifier = Modifier.Companion.width(width = 250.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.green),
                            contentColor = colorResource(id = R.color.white)
                        )
                    ) {
                        Text(
                            text = "Editar",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.labelSmall,
                            color = colorResource(id = R.color.white)
                        )
                    }
                    Button(
                        onClick = {
                            /*TODO*/
                        },
                        modifier = Modifier.Companion.width(width = 260.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.tintRed),
                            contentColor = colorResource(id = R.color.white)
                        )
                    ) {
                        Text(
                            text = "Desactivar",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.labelSmall,
                            color = colorResource(id = R.color.white)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        LazyColumn(contentPadding = innerPadding) {
            item {
                Box {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .height(height = 360.dp)
                    )

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        SettingsButtonIcons(
                            icon = Icons.Default.ArrowBackIosNew,
                            firstColor = colorResource(id = R.color.white),
                            secondColor = colorResource(id = R.color.white),
                            clickable = navController::popBackStack,
                            tintColor = colorResource(id = R.color.black),
                            modifier = Modifier.Companion
                                .align(Alignment.TopStart)
                                .padding(horizontal = 18.dp, vertical = 44.dp)
                                .size(size = 36.dp)
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = name,
                        fontSize = 40.sp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.W500
                        ),
                        lineHeight = 50.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    HorizontalDivider(
                        thickness = 0.6.dp,
                        color = colorResource(id = R.color.coolGrey)
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    if (description != null) {
                        Text(
                            text = description,
                            fontSize = 20.sp,
                            lineHeight = 28.sp,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.Companion.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                ProductAttributesRow(stock = stock, categoryId = categoryId)
            }
        }

    }
}

