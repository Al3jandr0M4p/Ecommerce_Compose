package com.clay.ecommerce_compose.ui.screens.client.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.UserRepository
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _businessProfile = MutableStateFlow<List<BusinessProfile?>>(emptyList())
    var businessState: StateFlow<List<BusinessProfile?>> = _businessProfile

    fun loadBusiness() {
        viewModelScope.launch {
            try {
                val result = userRepository.getAllBusiness()
                _businessProfile.value = result
                Log.d("HomeViewModel", "Los negocios se cargaron correctamente $result")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al cargar Los negocios ${e.message}")
            }
        }
    }

}
