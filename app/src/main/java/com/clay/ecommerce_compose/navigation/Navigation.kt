package com.clay.ecommerce_compose.navigation

import android.app.Application
import android.os.Build
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
import com.clay.ecommerce_compose.data.remote.HttpClientKtor
import com.clay.ecommerce_compose.data.remote.SupabaseConfig
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.ui.components.client.business.SearchInShop
import com.clay.ecommerce_compose.ui.screens.admin.categories.CategoriesScreen
import com.clay.ecommerce_compose.ui.screens.admin.dashboard.AdminDashboardScreen
import com.clay.ecommerce_compose.ui.screens.admin.delivery.DeliveryScreen
import com.clay.ecommerce_compose.ui.screens.admin.orders.OrdersScreen
import com.clay.ecommerce_compose.ui.screens.admin.reports.ReportsScreen
import com.clay.ecommerce_compose.ui.screens.admin.users.UsersScreen
import com.clay.ecommerce_compose.ui.screens.admin.users.UsersViewModel
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessAccountViewModel
import com.clay.ecommerce_compose.ui.screens.businesess.BusinessScreen
import com.clay.ecommerce_compose.ui.screens.businesess.product_details.BusinessProductDetails
import com.clay.ecommerce_compose.ui.screens.client.app_activity.TransactionsViewModel
import com.clay.ecommerce_compose.ui.screens.client.app_activity.WalletViewModel
import com.clay.ecommerce_compose.ui.screens.client.business.ModelViewUserBusiness
import com.clay.ecommerce_compose.ui.screens.client.business.UserBusinessScreen
import com.clay.ecommerce_compose.ui.screens.client.cart.Cart
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import com.clay.ecommerce_compose.ui.screens.client.cart.checkout.CheckOutScreen
import com.clay.ecommerce_compose.ui.screens.client.cart.checkout.CheckoutViewModel
import com.clay.ecommerce_compose.ui.screens.client.config.ConfigViewModel
import com.clay.ecommerce_compose.ui.screens.client.delivery.WaitingDelivery
import com.clay.ecommerce_compose.ui.screens.client.home.HomeViewModel
import com.clay.ecommerce_compose.ui.screens.client.home.UserHomeScreen
import com.clay.ecommerce_compose.ui.screens.client.search.SearchBar
import com.clay.ecommerce_compose.ui.screens.delivery.deliveryHome.DeliveryHomeScreen
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
    val cartViewModel: CartViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = "adminHome") {

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
            val supabase = SupabaseConfig.client
            val authRepository = AuthRepository(supabase = supabase)

            LoginScreen(
                navController = navController,
                viewModel = loginViewModel,
                authRepository = authRepository
            )
        }

        composable(route = "register") {
            val registerViewModel: RegisterViewModel = viewModel(factory = factory)

            RegisterScreen(
                viewModel = registerViewModel,
                navController = navController
            )
        }

        composable(route = "registerBusiness") {
            val businessViewModel: RegisterBusinessViewModel = viewModel(factory = factory)
            RegisterBusiness(
                viewModel = businessViewModel,
                navController = navController
            )
        }

        composable(route = "userHome") { backStackEntry ->
            val configViewModel: ConfigViewModel = viewModel(factory = factory)
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            val walletViewModel: WalletViewModel = viewModel(factory = factory)
            val transactionsViewModel: TransactionsViewModel = viewModel(factory = factory)

            UserHomeScreen(
                navController = navController,
                configViewModel = configViewModel,
                cartViewModel = cartViewModel,
                homeViewModel = homeViewModel,
                walletViewModel = walletViewModel,
                transactionsViewModel = transactionsViewModel
            )
        }

        composable(route = "details/{id}") { backStackEntry ->
            val idBusiness = backStackEntry.arguments?.getString("id")?.toInt()
            val userBusiness: ModelViewUserBusiness = viewModel(factory = factory)

            UserBusinessScreen(
                navController = navController,
                idBusiness = idBusiness,
                cartViewModel = cartViewModel,
                viewModel = userBusiness
            )
        }

        composable(route = "products/details/{businessId}/{id}") { backStackEntry ->
            val idProduct = backStackEntry.arguments?.getString("id")?.toInt()
            val idBusiness = backStackEntry.arguments?.getString("businessId")?.toInt()
            val businessAccountViewModel: BusinessAccountViewModel = viewModel(factory = factory)

            BusinessProductDetails(
                businessId = idBusiness,
                productId = idProduct,
                navController = navController,
                viewModel = businessAccountViewModel
            )
        }

        composable(route = "delivery/user") {
            WaitingDelivery(cartViewModel = cartViewModel, navController = navController)
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
            Cart(navController = navController, cartViewModel = cartViewModel)
        }

        composable(route = "search") {
            SearchBar(navController = navController, cartViewModel = cartViewModel)
        }

        composable(route = "searchInShop/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt()

            SearchInShop(idShop = id, navController = navController)
        }

        composable(route = "checkout/all") {
            val checkoutViewModel: CheckoutViewModel = viewModel(factory = factory)

            CheckOutScreen(cartViewModel = cartViewModel, navController = navController, checkoutViewModel = checkoutViewModel)
        }

        composable(route = "delivery") {
            DeliveryHomeScreen(navController = navController)
        }

        composable(route = "adminHome") {
            AdminDashboardScreen(
                onNavigateToUsers = { navController.navigate("adminUsers") },
                onNavigateToBusinesses = { navController.navigate("adminBusinesses") },
                onNavigateToReports = { navController.navigate("adminReports") },
                onNavigateToDelivery = { navController.navigate("adminDelivery") }
            )
        }

        composable(route = "adminDelivery") {
            DeliveryScreen(onBack = { navController.popBackStack() })
        }

        composable(route = "adminUsers") {
            val usersViewModel: UsersViewModel = viewModel(factory = factory)
            UsersScreen(
                onBack = { navController.popBackStack() },
                viewModel = usersViewModel
            )
        }

//        composable(route = "adminBusinesses") {
//            BusinessScreen(
//                onBack = { navController.popBackStack() }
//            )
//        }

        composable(route = "adminCategories") {
            CategoriesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = "adminOrders") {
            OrdersScreen(onBack = { navController.popBackStack() })
        }

        composable(route = "adminReports") {
            ReportsScreen(onBack = { navController.popBackStack() })
        }

    }
}
