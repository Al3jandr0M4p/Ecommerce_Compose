package com.clay.ecommerce_compose.ui.screens.client.app_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepository: WalletRepository): ViewModel() {

    private val _walletState = MutableStateFlow(value = WalletState())
    val walletState = _walletState.asStateFlow()

    fun recharge(amount: Double) {
        viewModelScope.launch {
            runCatching {
                walletRepository.addBalance(amount)
            }.onSuccess {
                loadWallet()
            }
        }
    }

    fun loadWallet() {
        viewModelScope.launch {
            _walletState.value = _walletState.value.copy(isLoading = true)

            runCatching {
                walletRepository.getWallet()
            }.onSuccess { wallet ->
                _walletState.value = WalletState(
                    balance = wallet.balance,
                    isLoading = false
                )
            }.onFailure { exception ->
                _walletState.value = WalletState(
                    error = exception.message,
                    isLoading = false
                )
            }
        }
    }
}