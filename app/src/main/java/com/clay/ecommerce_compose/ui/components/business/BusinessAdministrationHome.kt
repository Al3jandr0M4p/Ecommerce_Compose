package com.clay.ecommerce_compose.ui.components.business

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@Composable
fun BusinessAdministrationHome(
    business: BusinessProfile?,
    navController: NavHostController,
    viewModel: BusinessAccountViewModel
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            AsyncImage(
                model = business?.logoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 280.dp),
                onError = {
                    Log.e(
                        "BusinessInfoContent", "Error al cargar la imagen", it.result.throwable
                    )
                })
        }

        item {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                business?.name?.let {
                    Text(
                        text = it, style = MaterialTheme.typography.labelLarge, fontSize = 28.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Informacion",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.black)
                        )
                        Spacer(modifier = Modifier.width(width = 4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = colorResource(id = R.color.black)
                        )
                    }

                    if (business?.hasDelivery != true) {
                        TextButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.DeliveryDining,
                                contentDescription = null,
                                tint = colorResource(id = R.color.gold)
                            )
                            Spacer(modifier = Modifier.width(width = 4.dp))
                            Text(
                                text = "Entregas",
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.labelSmall,
                                color = colorResource(id = R.color.gold)
                            )
                            Spacer(modifier = Modifier.width(width = 4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = colorResource(id = R.color.gold)
                            )
                        }
                    }
                }
            }
        }

        item {
            if (business?.hasDelivery == true) {
                Column {
                    Text(
                        text = "Deliverys",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 20.sp
                    )

                    LazyRow {

                    }
                }
            }
        }

        item {
//            Text(text = "Telefono: ${business?.phone}")
//            BusinessLocationText(latitude = business?.latitud, longitude = business?.longitud)
//            Text(text =  "Horario: ${business?.horarioApertura} - ${business?.horarioCierre}")


            Text(text = "Cerrar session", modifier = Modifier.clickable {
                viewModel.signOut()
                navController.navigate(route = "login")
            })
        }
    }
}
