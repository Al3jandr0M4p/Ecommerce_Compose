package com.clay.ecommerce_compose.ui.screens.login

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import kotlinx.coroutines.delay


@Composable
fun LoginTextFields(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordChange: () -> Unit
) {

    val focusedPurple = Color(color = 0xFF6200EE)

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = {
            Text(text = "Email", style = MaterialTheme.typography.labelSmall)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
        },
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,

            focusedLabelColor = focusedPurple,
            unfocusedLabelColor = Color.Gray,

            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = focusedPurple,

            focusedLeadingIconColor = focusedPurple,
            unfocusedLeadingIconColor = Color.Gray
        ),
        shape = RoundedCornerShape(size = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(
                text = "Password",
                style = MaterialTheme.typography.labelSmall
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = onShowPasswordChange) {
                val visibilityIcon =
                    if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                Icon(
                    imageVector = visibilityIcon,
                    contentDescription = if (showPassword) "Hide password" else "Show password"
                )
            }
        },
        maxLines = 1,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,

            focusedLabelColor = focusedPurple,
            unfocusedLabelColor = Color.Gray,

            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = focusedPurple,

            focusedLeadingIconColor = focusedPurple,
            unfocusedLeadingIconColor = Color.Gray
        ),
        shape = RoundedCornerShape(size = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
    )

}


@Composable
fun BottomComp(
    modifier: Modifier = Modifier,
    onButtonAction: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    elevation: ButtonElevation? = null,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onButtonAction,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = elevation,
        contentPadding = PaddingValues(),
        content = content
    )
}


@Composable
fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel
) {
    var showPassword by remember { mutableStateOf(value = false) }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error, state.loggedIn) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        if (state.loggedIn) {
            val userRole = state.currentUser?.roleName ?: "guest"
            Toast.makeText(context, "Bienvenido ${state.currentUser}", Toast.LENGTH_SHORT).show()

            delay(300)
            when (userRole) {
                "usuario" -> navController.navigate(route = "userHome")
                else -> navController.navigate(route = "login")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_signin),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 350.dp)
        )

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
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(all = 20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LoginTextFields(
                    email = state.email,
                    onEmailChange = {
                        viewModel.handleIntent(
                            intent = LoginViewModel.Intent.EmailChanged(
                                email = it
                            )
                        )
                    },
                    password = state.password,
                    onPasswordChange = {
                        viewModel.handleIntent(
                            intent = LoginViewModel.Intent.PasswordChanged(
                                password = it
                            )
                        )
                    },
                    showPassword = showPassword,
                    onShowPasswordChange = { showPassword = !showPassword }
                )

                // login Buttom
                BottomComp(
                    onButtonAction = {
                        viewModel.handleIntent(intent = LoginViewModel.Intent.Submit)
                    },
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 20.dp)
                        .height(height = 56.dp),
                ) {
                    Text(
                        text = "Entrar",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }

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
