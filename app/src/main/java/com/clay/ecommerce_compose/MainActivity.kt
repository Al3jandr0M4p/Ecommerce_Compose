package com.clay.ecommerce_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clay.ecommerce_compose.data.repository.AppViewModelProvider
import com.clay.ecommerce_compose.ui.screens.SplashScreen
import com.clay.ecommerce_compose.ui.screens.cliente.Cart
import com.clay.ecommerce_compose.ui.screens.cliente.UserHomeScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterScreen
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.theme.Ecommerce_ComposeTheme
import com.clay.ecommerce_compose.ui.screens.admin.AdminDashboardScreen
import com.clay.ecommerce_compose.ui.screens.admin.CategoriesScreen
import com.clay.ecommerce_compose.ui.screens.admin.OrdersScreen
import com.clay.ecommerce_compose.ui.screens.admin.ReportsScreen
import com.clay.ecommerce_compose.ui.screens.admin.UsersScreen
import com.clay.ecommerce_compose.ui.screens.admin.BusinessScreen
import com.clay.ecommerce_compose.ui.screens.admin.ProductsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            Ecommerce_ComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0.dp)
                ) { innerPadding ->
                    Navigation(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = "adminHome") {

            composable(route = "splash") {
                val mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider)
                SplashScreen(
                    modifier = Modifier,
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }

            composable(route = "login") {
                val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider)
                LoginScreen(
                    navController = navController,
                    modifier = Modifier,
                    viewModel = loginViewModel
                )
            }

            composable(route = "register") {
                val registerViewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider)
                RegisterScreen(
                    viewModel = registerViewModel,
                    navController = navController,
                    modifier = Modifier
                )
            }

            composable(route = "userHome") {
                UserHomeScreen(
                    modifier = Modifier,
                    navController = navController,
                    animatedVisibilityScope = this
                )
            }

            composable(route = "cart") {
                Cart()
            }

            // ADMIN ROUTES
            composable(route = "adminHome") {
                AdminDashboardScreen(
                    onNavigateToUsers = { navController.navigate("adminUsers") },
                    onNavigateToBusinesses = { navController.navigate("adminBusinesses") },
                    onNavigateToProducts = { navController.navigate("adminProducts") },
                    onNavigateToCategories = { navController.navigate("adminCategories") },
                    onNavigateToOrders = { navController.navigate("adminOrders") },
                    onNavigateToReports = { navController.navigate("adminReports") }
                )
            }

            composable(route = "adminUsers") {
                UsersScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = "adminBusinesses") {
                BusinessScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = "adminProducts") {
                ProductsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = "adminCategories") {
                CategoriesScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = "adminOrders") {
                OrdersScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = "adminReports") {
                ReportsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}