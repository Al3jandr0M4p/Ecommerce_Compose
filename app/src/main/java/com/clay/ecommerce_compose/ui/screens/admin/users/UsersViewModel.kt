package com.clay.ecommerce_compose.ui.screens.admin.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.UserRepository
import com.clay.ecommerce_compose.domain.model.User
import com.clay.ecommerce_compose.domain.model.UserToInsert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            val profiles = userRepository.getAllUsers()
            _users.value = profiles.map {
                User(
                    id = it.id,
                    name = it.name ?: "Sin nombre",
                    email = it.email ?: "Sin email",
                    role = it.roleName ?: "Sin rol",
                    status = "Activo"
                )
            }
        }
    }

    fun createUser(name: String, email: String, role: String, password: String) {
        viewModelScope.launch {
            userRepository.createUser(
                UserToInsert(
                    email = email,
                    password = password,
                    userName = name,
                    roleName = role
                )
            )
            loadUsers()
        }
    }
}
