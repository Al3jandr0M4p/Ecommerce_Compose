package com.clay.ecommerce_compose.ui.screens.cliente

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.Tabs
import com.clay.ecommerce_compose.ui.components.CategoriesSection
import com.clay.ecommerce_compose.ui.components.Chips
import com.clay.ecommerce_compose.ui.components.HeaderUserHome
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
fun Configuration(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 14.dp)
        ) {
            Text(
                text = "Configuracion",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 28.sp
            )
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 14.dp)
                .clickable {
                    // TODO not yet implemented
                },
            shape = RoundedCornerShape(size = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(size = 50.dp)
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Column {
                    Text(
                        text = "Alejandro Alvares",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "william.henry.harrison@pet-store.com",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.weight(weight = 1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = 24.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 14.dp)
                .clickable {
                    // TODO not yet implemented
                },
            shape = RoundedCornerShape(size = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = "Cerrar sesion",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = 24.dp)
                )
            }
        }
    }
}


@Composable
fun UserHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var selectedTab by remember { mutableStateOf<Tabs>(value = Tabs.Home) }
    val tabs = listOf(Tabs.Home, Tabs.Configuracion)

    Scaffold(
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues()
            ) {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        icon = {
                            Icon(imageVector = tab.icon, contentDescription = tab.title)
                        },
                        label = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 16.sp
                            )
                        },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
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

                is Tabs.Configuracion -> Configuration(navController = navController)
            }
        }
    }
}
