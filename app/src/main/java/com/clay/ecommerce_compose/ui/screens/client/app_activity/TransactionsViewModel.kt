package com.clay.ecommerce_compose.ui.screens.client.app_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.WalletRepository
import com.clay.ecommerce_compose.data.repository.WalletTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TransactionsState(
    val transactions: List<WalletTransaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TransactionsViewModel(private val walletRepository: WalletRepository) : ViewModel() {
    private val _transactionsState = MutableStateFlow(TransactionsState())
    val transactionsState = _transactionsState.asStateFlow()

    fun loadTransctions() {
        viewModelScope.launch {
            _transactionsState.value = _transactionsState.value.copy(isLoading = true)

            runCatching {
                walletRepository.getTransactions()
            }.onSuccess { transactions ->
                _transactionsState.value = TransactionsState(
                    transactions = transactions,
                    isLoading = false
                )
            }.onFailure { exception ->
                _transactionsState.value = TransactionsState(
                    error = exception.message,
                    isLoading = false
                )
            }
        }
    }


}