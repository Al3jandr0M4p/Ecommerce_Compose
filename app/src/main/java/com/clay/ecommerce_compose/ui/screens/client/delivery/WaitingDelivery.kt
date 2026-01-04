package com.clay.ecommerce_compose.ui.screens.client.delivery

import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.client.cart.CartStepper
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaitingDelivery(cartViewModel: CartViewModel, navController: NavHostController) {
    val state by cartViewModel.state.collectAsState()
    val context = LocalContext.current

    val activeOrderId by cartViewModel.state.map { it.activeOrder?.id }
        .collectAsState(initial = null)

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val progress by remember {
        derivedStateOf {
            when (sheetState.currentValue) {
                SheetValue.Expanded -> 1f
                SheetValue.PartiallyExpanded -> 0f
                SheetValue.Hidden -> 0f
            }
        }
    }

    val fabAlpha by animateFloatAsState(
        targetValue = 1f - progress,
        label = "fabAlpha"
    )

    val fabScale by animateFloatAsState(
        targetValue = if (progress > 0.3f) 0.85f else 1f,
        label = "fabScale"
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 130.dp,
        topBar = {
            Column(modifier = Modifier.background(color = colorResource(id = R.color.white))) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Delivery",
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.white)
                    ),
                )

                CartStepper(
                    steps = listOf("Carrito", "Checkout", "Delivery"),
                    currentStep = 2
                )
            }
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tu pedido va en camino",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = {
                    Log.d("DELIVERY_SCREEN", "Botón 'Obtener factura' presionado")
                    if (activeOrderId != null) {
                        Log.d("DELIVERY_SCREEN", "Active order ID: $activeOrderId")
                        val url =
                            "https://6gszhspz-3002.use2.devtunnels.ms/api/orders/$activeOrderId/invoice/pdf"
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    } else {
                        Log.d("DELIVERY_SCREEN", "No hay orden activa")
                    }
                }) {
                    Text(text = "Obtener factura")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = rememberMapViewportState {
                    setCameraOptions {
                        center(Point.fromLngLat(-69.9312, 18.4861))
                        zoom(12.0)
                        pitch(0.0)
                    }
                },
            )

            FloatingActionButton(
                onClick = {
                    navController.navigate(route = "userHome")
                },
                containerColor = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .graphicsLayer {
                        alpha = fabAlpha
                        scaleX = fabScale
                        scaleY = fabScale
                    }
            ) {
                Icon(imageVector = Icons.Default.Home, contentDescription = null)
            }
        }
    }
}
