package com.clay.ecommerce_compose.activity

import com.clay.ecommerce_compose.domain.model.UserSession


sealed class SplashState {
    object Loading : SplashState()
    object NotAuthenticated : SplashState()

    data class Authenticated(val session: UserSession?) : SplashState()
}
