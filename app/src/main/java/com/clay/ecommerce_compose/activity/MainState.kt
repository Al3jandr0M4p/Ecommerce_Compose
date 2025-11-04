package com.clay.ecommerce_compose.activity

import com.clay.ecommerce_compose.domain.model.UserSession


sealed class SplashState {
    object Loading : SplashState()
    data class Success(val session: UserSession?) : SplashState()
}
