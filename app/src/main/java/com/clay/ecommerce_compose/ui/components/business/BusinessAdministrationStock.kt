package com.clay.ecommerce_compose.ui.components.business

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessAdministrationStock(
    navController: NavHostController,
    profile: BusinessProfile?,
    viewModel: BusinessAccountViewModel,
    openSheet: Boolean,
    sheetState: SheetState,
    onCloseSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val view by viewModel.state.collectAsState()

//        repeat(6) { index ->
//            ProductsCards(
//                id = profile?.id,
//                name = profile?.name,
//                price = ,
//                imageUrl = R.drawable.ic_launcher_background,
//                navController = navController
//            )
//        }

        if (openSheet) {
            Stepeer(
                SheetState = sheetState,
                onDimiss = { onCloseSheet() },
                viewModel = viewModel,
                businessId = view.businessId
            )
        }
    }
}
