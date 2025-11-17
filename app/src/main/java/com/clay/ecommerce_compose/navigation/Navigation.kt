package com.clay.ecommerce_compose.navigation

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.clay.ecommerce_compose.data.remote.SupabaseConfig
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.ui.components.client.business.SearchInShop
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessScreen
import com.clay.ecommerce_compose.ui.screens.client.business.UserBusinessScreen
import com.clay.ecommerce_compose.ui.screens.client.cart.Cart
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.ui.screens.client.config.ConfigViewModel
import com.clay.ecommerce_compose.ui.screens.client.home.HomeViewModel
import com.clay.ecommerce_compose.ui.screens.client.home.UserHomeScreen
import com.clay.ecommerce_compose.ui.screens.client.search.SearchBar
import com.clay.ecommerce_compose.ui.screens.login.LoginScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterScreen
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusiness
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusinessViewModel


@RequiresApi(Build.VERSION_CODES.S)
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
                modifier = Modifier, navController = navController, mainViewModel = mainViewModel
            )
        }

        composable(route = "login") {
            val loginViewModel: LoginViewModel = viewModel(factory = factory)
            val supabase = SupabaseConfig.client
            val authRepository = AuthRepository(supabase = supabase)

            LoginScreen(
                navController = navController,
                modifier = Modifier,
                viewModel = loginViewModel,
                authRepository = authRepository
            )
        }

        composable(route = "register") {
            val registerViewModel: RegisterViewModel = viewModel(factory = factory)

            RegisterScreen(
                viewModel = registerViewModel, navController = navController, modifier = Modifier
            )
        }

        composable(route = "registerBusiness") {
            val businessViewModel: RegisterBusinessViewModel = viewModel(factory = factory)
            RegisterBusiness(viewModel = businessViewModel, navController = navController)
        }

        composable(route = "userHome") {
            val configViewModel: ConfigViewModel = viewModel(factory = factory)
            val cartViewModel: CartViewModel = viewModel(factory = factory)
            val homeViewModel: HomeViewModel = viewModel(factory = factory)

            UserHomeScreen(
                modifier = Modifier,
                navController = navController,
                configViewModel = configViewModel,
                cartViewModel = cartViewModel,
                homeViewModel = homeViewModel
            )
        }

        composable(route = "details/{id}") { backStackEntry ->
            val idBusiness = backStackEntry.arguments?.getString("id")?.toInt()
            Log.d("IdBusiness", "same id in navigation $idBusiness")

            UserBusinessScreen(
                navController = navController,
                idBusiness = idBusiness,
                cartViewModel = viewModel(factory = factory)
            )
        }

        composable(route = "businessHome/{businessId}") { backStackEntry ->
            val businessAccountViewModel: BusinessAccountViewModel = viewModel(factory = factory)
            val businessId = backStackEntry.arguments?.getString("businessId") ?: ""

            BusinessScreen(
                businessId = businessId,
                navController = navController,
                viewModel = businessAccountViewModel
            )
        }

        composable(route = "cart") {
            val cartViewModel: CartViewModel = viewModel(factory = factory)
            Cart(navController = navController, modifier = Modifier, cartViewModel = cartViewModel)
        }

        composable(route = "search") {
            val cartViewModel: CartViewModel = viewModel(factory = factory)
            SearchBar(navController = navController, cartViewModel = cartViewModel)
        }

        composable(route = "searchInShop/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt()
            SearchInShop(idShop = id, navController = navController)
        }

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
