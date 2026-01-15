package com.clay.ecommerce_compose.domain.usecase

import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.domain.model.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetCurrentUserSessionUseCase(private val authRepository: AuthRepository) {

    private val _session = MutableStateFlow<UserSession?>(null)
    val session = _session.asStateFlow()

    suspend fun loadSession()  {
        val profile = authRepository.getCurrentUserProfile()

        _session.value = profile?.let {
            UserSession(
                id = it.id,
                role = it.roleName,
                businessId = it.businessId
            )
        }
    }

    operator fun invoke(): StateFlow<UserSession?> = session

}
