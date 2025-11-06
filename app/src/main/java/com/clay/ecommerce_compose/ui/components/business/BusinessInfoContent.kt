package com.clay.ecommerce_compose.ui.components.business

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@Composable
fun BusinessInfoContent(
    business: BusinessProfile?,
    navController: NavHostController,
    viewModel: BusinessAccountViewModel
) {
    Column {
        AsyncImage(
            model = business?.logoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 280.dp),
            onError = {
                Log.e("BusinessInfoContent", "Error al cargar la imagen", it.result.throwable)
            }
        )
        Text(text = "✅ Negocio: ${business?.name}")
        Text(text = "📍 Dirección: ${business?.direccion}")
        Text(text = "📞 Teléfono: ${business?.phone}")
        Text(text = "🕒 Horario: ${business?.horarioApertura} - ${business?.horarioCierre}")
        Text(text = "🚚 Delivery: ${if (business?.hasDelivery == true) "Sí" else "No"}")

        Text(text = "Cerrar session", modifier = Modifier.clickable {
            viewModel.signOut()
            navController.navigate (route = "login")
        })
    }
}
