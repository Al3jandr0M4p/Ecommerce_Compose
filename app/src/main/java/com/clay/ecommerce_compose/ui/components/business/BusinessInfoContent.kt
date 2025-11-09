package com.clay.ecommerce_compose.ui.components.business

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@Composable
fun BusinessInfoContent(
    business: BusinessProfile?,
    navController: NavHostController,
    viewModel: BusinessAccountViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .height(height = 180.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                AsyncImage(
                    model = business?.logoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size = 110.dp)
                        .clip(shape = CircleShape)
                        .shadow(elevation = 6.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 150.dp),
                    onError = {
                        Log.e(
                            "BusinessInfoContent",
                            "Error al cargar la imagen",
                            it.result.throwable
                        )
                    }
                )
            }
        }
//        Text(text = "✅ Negocio: ${business?.name}")
//        Text(text = "📍 Dirección: ${business?.direccion}")
//        Text(text = "📞 Teléfono: ${business?.phone}")
//        Text(text = "🕒 Horario: ${business?.horarioApertura} - ${business?.horarioCierre}")
//        Text(text = "🚚 Delivery: ${if (business?.hasDelivery == true) "Sí" else "No"}")


        item {
            Text(text = "Cerrar session", modifier = Modifier.clickable {
                viewModel.signOut()
                navController.navigate(route = "login")
            })
        }
    }
}
