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

class UsersViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            val profiles = userRepository.getAllUsers()
            _users.value = profiles.mapNotNull { profile ->
                // Mapeamos el objeto Profile de la capa de datos al objeto User de la UI
                profile.id?.let {
                    User(
                        id = it,
                        name = profile.name ?: "Sin nombre",
                        email = profile.email ?: "Sin email",
                        role = profile.roleName ?: "Sin rol", // Asumiendo que quieres mostrar el nombre del rol
                        status = "Activo" // El status no está en el perfil, lo dejamos como "Activo" por ahora
                    )
                }
            }
        }
    }

    fun createUser(name: String, email: String, role: String, password: String) {
        viewModelScope.launch {
            val userToInsert = UserToInsert(email = email, password = password, userName = name, roleName = role)
            userRepository.createUser(userToInsert)
            loadUsers()
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            // TODO: Llamar al UserRepository para actualizar el usuario
            // Esto puede requerir políticas de RLS o una función RPC en Supabase.
            loadUsers()
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            // TODO: Llamar al UserRepository para eliminar el usuario
            // Esto requerirá una función RPC en Supabase para eliminar el auth user y el profile.
            loadUsers()
        }
    }
}