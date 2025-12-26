package com.clay.ecommerce_compose.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.clay.ecommerce_compose.navigation.Navigation
import com.clay.ecommerce_compose.ui.components.network.rememberNetworkStatus
import com.clay.ecommerce_compose.ui.screens.internet.NoInternetScreen
import com.clay.ecommerce_compose.ui.theme.Ecommerce_ComposeTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val isNetworkAvailable = rememberNetworkStatus()

            Ecommerce_ComposeTheme {
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    if (isNetworkAvailable) {
                        Navigation(
                            navController = navController,
                            modifier = Modifier.Companion.padding(paddingValues = innerPadding)
                        )
                    } else {
                        NoInternetScreen()
                    }
                }
            }
        }
    }
}
