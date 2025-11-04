package com.clay.ecommerce_compose.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clay.ecommerce_compose.activity.MainViewModel
import com.clay.ecommerce_compose.activity.SplashScreen
import com.clay.ecommerce_compose.data.AppViewModelProvider
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessScreen
import com.clay.ecommerce_compose.ui.screens.client.business.UserBusinessScreen
import com.clay.ecommerce_compose.ui.screens.client.cart.Cart
import com.clay.ecommerce_compose.ui.screens.client.config.ConfigViewModel
import com.clay.ecommerce_compose.ui.screens.client.home.UserHomeScreen
import com.clay.ecommerce_compose.ui.screens.client.search.SearchBar
import com.clay.ecommerce_compose.ui.screens.login.LoginScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterScreen
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.BusinessViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusiness


@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val application = LocalContext.current.applicationContext as Application
    val factory = AppViewModelProvider(application)

    NavHost(navController = navController, startDestination = "splash") {

        composable(route = "splash") {
            val mainViewModel: MainViewModel = viewModel(factory = factory)
            SplashScreen(
                modifier = Modifier,
                navController = navController,
                mainViewModel = mainViewModel
            )
        }

        composable(route = "login") {
            val loginViewModel: LoginViewModel = viewModel(factory = factory)
            LoginScreen(
                navController = navController,
                modifier = Modifier,
                viewModel = loginViewModel
            )
        }

        composable(route = "register") {
            val registerViewModel: RegisterViewModel = viewModel(factory = factory)
            RegisterScreen(
                viewModel = registerViewModel,
                navController = navController,
                modifier = Modifier
            )
        }

        composable(route = "registerBusiness") {
            val businessViewModel: BusinessViewModel = viewModel(factory = factory)
            RegisterBusiness(viewModel = businessViewModel, navController = navController)
        }

        composable(route = "userHome") {
            val configViewModel: ConfigViewModel = viewModel(factory = factory)
            UserHomeScreen(
                modifier = Modifier,
                navController = navController,
                configViewModel = configViewModel
            )
        }

        composable(route = "details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt()
            UserBusinessScreen(navController = navController, id = id, modifier = Modifier)
        }

        composable(route = "businessHome") {
            BusinessScreen(modifier = modifier)
        }

        composable(route = "cart") {
            Cart(modifier = Modifier)
        }

        composable(route = "search") {
            SearchBar()
        }

//            composable(route = "details/{id}") { backStackEntry ->
//                val id = backStackEntry.arguments?.getString("id")?.toInt()
//                DetailsScreen(
//                    id = id,
//                    navController = navController,
//                    animatedVisibilityScope = this
//                )
//            }

//        composable(route = "adminHome") {
//            UserHomeScreen(modifier = modifier)
//        }
//
//
//        composable(route = "deliveryHome") {
//            UserHomeScreen(modifier = modifier)
//        }

    }
}
