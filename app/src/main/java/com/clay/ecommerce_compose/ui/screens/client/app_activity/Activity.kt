package com.clay.ecommerce_compose.ui.screens.client.app_activity

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.clay.ecommerce_compose.ui.components.client.header.HeaderActivity

@Composable
fun Activity(walletViewModel: WalletViewModel) {
    val state by walletViewModel.walletState.collectAsState()

    LaunchedEffect(Unit) {
        walletViewModel.loadWallet()
    }

    Scaffold { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            item {
                HeaderActivity(
                    balance = state.balance,
                    isLoading = state.isLoading
                )
            }
        }
    }
}
