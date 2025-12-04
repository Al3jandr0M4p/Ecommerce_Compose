package com.clay.ecommerce_compose.utils.hooks

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.clay.ecommerce_compose.navigation.Tabs
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel
import com.clay.ecommerce_compose.ui.screens.businesess.UIEvents
import kotlinx.coroutines.launch

data class BusinessScreenController @OptIn(ExperimentalMaterial3Api::class) constructor(
    val selectedTab: MutableState<Tabs>,
    val openSheet: MutableState<Boolean>,
    val sheetState: SheetState,
    val snackBarHost: SnackbarHostState
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun useBusinessScreen(viewModel: BusinessAccountViewModel): BusinessScreenController {
    val selectedTab = remember { mutableStateOf<Tabs>(value = Tabs.Home) }
    val openSheet = remember { mutableStateOf(value = false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackBarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvents.ShowMessage -> {
                    scope.launch {
                        snackBarHost.showSnackbar(message = event.message)
                    }
                }
                is UIEvents.CloseSheet -> openSheet.value = false
            }
        }
    }

    return BusinessScreenController(
        selectedTab = selectedTab,
        openSheet = openSheet,
        sheetState = sheetState,
        snackBarHost = snackBarHost
    )
}
