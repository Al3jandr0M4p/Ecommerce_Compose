package com.clay.ecommerce_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clay.ecommerce_compose.data.repository.AppViewModelProvider
import com.clay.ecommerce_compose.ui.screens.SplashScreen
import com.clay.ecommerce_compose.ui.screens.cliente.Cart
import com.clay.ecommerce_compose.ui.screens.cliente.DetailsScreen
import com.clay.ecommerce_compose.ui.screens.cliente.UserHomeScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterScreen
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.theme.Ecommerce_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            Ecommerce_ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    NavHost(navController = navController, startDestination = "splash") {

        composable(route = "splash") {
            SplashScreen(modifier = modifier) {
                navController.navigate(route = "login") {
                    popUpTo(route = "splash") {
                        inclusive = true
                    }
                }
            }
        }

        composable(route = "login") {
            val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider)
            LoginScreen(
                navController = navController,
                modifier = modifier,
                viewModel = loginViewModel
            )
        }

        composable(route = "register") {
            val registerViewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider)
            RegisterScreen(
                viewModel = registerViewModel,
                navController = navController,
                modifier = modifier
            )
        }

        composable(route = "userHome") {
            UserHomeScreen(modifier = modifier, navController = navController)
        }

        composable(route = "details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt()
            DetailsScreen(id = id, navController = navController)
        }

        composable(route = "cart") {
            Cart()
        }

//        composable(route = "adminHome") {
//            UserHomeScreen(modifier = modifier)
//        }

//        composable(route = "negocio") {
//            UserHomeScreen(modifier = modifier)
//        }
//
//        composable(route = "sellerHome") {
//            UserHomeScreen(modifier = modifier)
//        }
//
//        composable(route = "deliveryHome") {
//            UserHomeScreen(modifier = modifier)
//        }

    }
}
