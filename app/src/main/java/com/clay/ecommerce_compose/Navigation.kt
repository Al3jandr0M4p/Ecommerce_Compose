package com.clay.ecommerce_compose

import android.app.Application
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clay.ecommerce_compose.data.AppViewModelProvider
import com.clay.ecommerce_compose.ui.screens.signOut.SignOut
import com.clay.ecommerce_compose.ui.screens.SplashScreen
import com.clay.ecommerce_compose.ui.screens.cliente.UserHomeScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginScreen
import com.clay.ecommerce_compose.ui.screens.login.LoginViewModel
import com.clay.ecommerce_compose.ui.screens.register.RegisterScreen
import com.clay.ecommerce_compose.ui.screens.register.RegisterViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.BusinessViewModel
import com.clay.ecommerce_compose.ui.screens.register.business.RegisterBusiness
import com.clay.ecommerce_compose.ui.screens.signOut.ConfigViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val application = LocalContext.current.applicationContext as Application
    val factory = AppViewModelProvider(application)


    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = "register") {

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
                RegisterBusiness(viewModel = businessViewModel)
            }

            composable(route = "userHome") {
                UserHomeScreen(
                    modifier = Modifier,
                    navController = navController,
                    animatedVisibilityScope = this,
                )
            }

            composable(route = "signout") {
                val signOutModel: ConfigViewModel = viewModel(factory = factory)
                SignOut(navController = navController, signOutModel = signOutModel)
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
}
