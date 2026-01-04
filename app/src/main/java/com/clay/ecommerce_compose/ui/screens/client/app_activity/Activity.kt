package com.clay.ecommerce_compose.ui.screens.client.app_activity

//noinspection SuspiciousImport
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.ui.components.client.cart.TransactionItem
import com.clay.ecommerce_compose.ui.components.client.header.HeaderActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Activity(walletViewModel: WalletViewModel, transactionsViewModel: TransactionsViewModel) {
    val walletState by walletViewModel.walletState.collectAsState()
    val transactionsState by transactionsViewModel.transactionsState.collectAsState()

    LaunchedEffect(Unit) {
        walletViewModel.loadWallet()
        transactionsViewModel.loadTransctions()
    }

    LazyColumn {
        item {
            HeaderActivity(
                balance = walletState.balance,
                isLoading = walletState.isLoading
            )
        }

        item {
            Text(text = "Anteriores") // compras anteriores en la app
        }

        item {
            Text(
                text = "Historial de pagos",
                fontSize = 28.sp,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.Companion.padding(start = 16.dp)
            )
        }

        if (transactionsState.isLoading) {
            item {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(all = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (transactionsState.transactions.isEmpty() && !transactionsState.isLoading) {
            item {
                Text(
                    text = "No hay transacciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.Companion.padding(all = 16.dp)
                )
            }
        }

        items(transactionsState.transactions) { transaction ->
            TransactionItem(transaction = transaction)
        }
    }
}
