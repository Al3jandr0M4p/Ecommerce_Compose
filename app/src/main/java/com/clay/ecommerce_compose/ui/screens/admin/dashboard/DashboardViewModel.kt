package com.clay.ecommerce_compose.ui.screens.admin.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.AuthRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loggedOut = mutableStateOf(false)
    val loggedOut: State<Boolean> = _loggedOut

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _loggedOut.value = true
        }
    }
}

