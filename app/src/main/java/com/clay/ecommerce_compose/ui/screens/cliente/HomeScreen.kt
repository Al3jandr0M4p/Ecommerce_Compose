package com.clay.ecommerce_compose.ui.screens.cliente

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.data.Tabs
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.ui.components.CategoriesSection
import com.clay.ecommerce_compose.ui.components.Chips
import com.clay.ecommerce_compose.ui.components.HeaderUserHome
import com.clay.ecommerce_compose.ui.components.MyBotomAppBar
import com.clay.ecommerce_compose.ui.components.SearchBar

@Composable
fun Home(navController: NavHostController, animated: AnimatedVisibilityScope) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item { HeaderUserHome(navController = navController) }

        item { SearchBar() }

        item { CategoriesSection(navController = navController) }

        item { Spacer(modifier = Modifier.height(height = 16.dp)) }

        item { Chips() }

//        item {
//            SharedCardTransition(
//                navController = navController,
//                animatedVisibilityScope = animated
//            )
//        }
    }
}


@Composable
fun UserHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    var selectedTab by remember { mutableStateOf<Tabs>(value = Tabs.Home) }


    Scaffold(
        bottomBar = {
            MyBotomAppBar(
                selectedTab = selectedTab,
                onTabSelected = { newTab ->
                    selectedTab = newTab
                }
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            when (selectedTab) {
                is Tabs.Home -> Home(
                    navController = navController,
                    animated = animatedVisibilityScope
                )

                is Tabs.ShoppinCart -> Cart()

                is Tabs.Configuracion -> Configuration(
                    navController = navController,
                )
            }
        }
    }
}
