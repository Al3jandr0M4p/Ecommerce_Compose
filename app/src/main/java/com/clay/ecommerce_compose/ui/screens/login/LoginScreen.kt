package com.clay.ecommerce_compose.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.data.repository.AuthRepository
import com.clay.ecommerce_compose.domain.model.UserSession
import com.clay.ecommerce_compose.domain.usecase.GetCurrentUserSessionUseCase
import com.clay.ecommerce_compose.ui.components.auth.login_text_fields.LoginTextFields
import com.clay.ecommerce_compose.ui.components.auth.social_login_component.SocialLoginComponent
import com.clay.ecommerce_compose.ui.components.auth.users.BottomComp
import com.clay.ecommerce_compose.ui.screens.client.cart.CartViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
) {
    var showPassword by remember { mutableStateOf(value = false) }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error, state.loggedIn) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        if (state.loggedIn) {
            navController.navigate("splash") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white))
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.ic_signin),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 380.dp)
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(all = 14.dp)
            ) {
                Text(
                    text = "Bienvenido",
                    fontSize = 32.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "de vuelta",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.coolGrey)
                )
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.white))
                    .padding(all = 20.dp)
            ) {
                SocialLoginComponent()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "Tu Primera vez?",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            text = "Crea una cuenta",
                            fontSize = 16.sp,
                            color = Color(color = 0xFF6200EE),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .clickable { navController.navigate(route = "register") }
                                .padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
