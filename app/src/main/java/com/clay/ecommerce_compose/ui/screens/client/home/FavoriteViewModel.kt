package com.clay.ecommerce_compose.ui.screens.client.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FavoriteViewModel : ViewModel() {

    private val _favorites = MutableStateFlow<Set<String>>(value = emptySet())
    val favorites = _favorites.asStateFlow()

    fun toggleFavorite(businessId: String) {
        _favorites.update { current ->
            if (current.contains(businessId)) {
                current - businessId
            } else {
                current + businessId
            }
        }
    }

    fun isFavorite(businessId: String): Boolean {
        return _favorites.value.contains(businessId)
    }

}
