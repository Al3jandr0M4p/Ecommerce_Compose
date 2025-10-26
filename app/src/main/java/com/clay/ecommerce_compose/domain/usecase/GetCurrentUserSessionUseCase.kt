package com.clay.ecommerce_compose.domain.usecase

import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.domain.model.UserSession

class GetCurrentUserSessionUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(): UserSession? {
        val profile = authRepository.getCurrentUserProfile()

        return profile?.let {
            UserSession(
                id = it.id,
                role = it.roleName
            )
        }
    }

}
