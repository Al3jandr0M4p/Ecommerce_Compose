package com.clay.ecommerce_compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.clay.ecommerce_compose.data.repository.AppViewModelProvider
import com.clay.ecommerce_compose.ui.screens.login.LoginScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun buttonClickShowsMessage() {
        composeTestRule.setContent {
            LoginScreen(rememberNavController(), viewModel = viewModel(factory = AppViewModelProvider))
        }

        composeTestRule.onNodeWithText("Iniciar Sesion").performClick()
        composeTestRule.onNodeWithText("Bienvenido").assertExists()
    }
}