package com.clay.ecommerce_compose.ui.components.business.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
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
