package com.clay.ecommerce_compose.ui.screens.client.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.UserRepository
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive

class ConfigViewModel(private val userRepository: UserRepository) : ViewModel() {
    var userInfo by mutableStateOf<UserInfo?>(null)
        private set

    var userName by mutableStateOf("")
        private set

    fun getUserInfoById() {
        viewModelScope.launch {
            userName =
                userRepository.getUserInfoById().userMetadata?.get("username")?.jsonPrimitive?.content
                    ?: ""
            userInfo = userRepository.getUserInfoById()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
        }
    }
}
