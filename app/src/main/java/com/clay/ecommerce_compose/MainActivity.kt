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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clay.ecommerce_compose.screens.LoginScreen
import com.clay.ecommerce_compose.screens.SplashScreen
import com.clay.ecommerce_compose.screens.cliente.UserHomeScreen
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
                        modifier = Modifier.padding(paddingValues = innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier = Modifier, navController: NavHostController) {
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
            LoginScreen(modifier = modifier) {
                navController.navigate(route = "userHome")
            }
        }

        composable(route = "userHome") {
            UserHomeScreen(modifier = modifier)
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
