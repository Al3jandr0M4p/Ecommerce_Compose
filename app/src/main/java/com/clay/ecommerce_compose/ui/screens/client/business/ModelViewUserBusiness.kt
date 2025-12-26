package com.clay.ecommerce_compose.ui.screens.client.business

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.ecommerce_compose.data.repository.BusinessRepository
import com.clay.ecommerce_compose.domain.model.BusinessProfile
import com.clay.ecommerce_compose.domain.model.ProductPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ModelViewUserBusiness(val businessRepository: BusinessRepository) : ViewModel() {
    private val _businessInfo = MutableStateFlow<BusinessProfile?>(value = null)
    val businessInfo: StateFlow<BusinessProfile?> = _businessInfo.asStateFlow()

    private val _businessProductsInfo = MutableStateFlow<List<ProductPayload>>(value = emptyList())
    val businessProductsInfo: StateFlow<List<ProductPayload>> = _businessProductsInfo.asStateFlow()

    private var lastBusinessId: Int? = null

    fun getBusinessInfoById(id: Int?) {
        viewModelScope.launch {
            val result = businessRepository.getBusinessInfoByID(id)
            _businessInfo.value = result
        }
    }

    fun getProductsByBusinessId(id: Int?) {
        if (id == null || id == lastBusinessId) return
        lastBusinessId = id

        viewModelScope.launch {
            _businessProductsInfo.value =
                businessRepository.getProductsByBusinessId(id)
        }
    }
}
