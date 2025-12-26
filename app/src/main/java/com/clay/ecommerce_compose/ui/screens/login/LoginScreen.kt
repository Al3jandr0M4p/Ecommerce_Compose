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
import com.clay.ecommerce_compose.ui.components.auth.users.BottomComp
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
            Text(
                text = "Email",
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelSmall
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null
            )
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
                fontSize = 12.sp,
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
fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    authRepository: AuthRepository,
) {
    var showPassword by remember { mutableStateOf(value = false) }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val session = remember { mutableStateOf<UserSession?>(null) }

    LaunchedEffect(state.error, state.loggedIn) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        if (state.loggedIn) {
            val getSessionUseCase = GetCurrentUserSessionUseCase(authRepository = authRepository)
            session.value = getSessionUseCase.invoke()

            val userRole = session.value?.role ?: "guest"

            Toast.makeText(context, "Bienvenido ${session.value?.role}", Toast.LENGTH_SHORT)
                .show()

            delay(300)
            when (userRole) {
                "usuario" -> {
                    navController.navigate(route = "userHome")
                }

                "negocio" -> {
                    val businessId = session.value?.businessId
                    navController.navigate(route = "businessHome/${businessId}")
                }

                "admin" -> {
                    navController.navigate(route = "adminHome")
                }

                else -> {
                    Toast.makeText(context, "Rol no reconocido: $userRole", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate(route = "login")
                }
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LoginTextFields(
                        email = state.email,
                        onEmailChange = {
                            viewModel.handleIntent(
                                intent = Intent.EmailChanged(
                                    email = it
                                )
                            )
                        },
                        password = state.password,
                        onPasswordChange = {
                            viewModel.handleIntent(
                                intent = Intent.PasswordChanged(
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
                            if (!state.isLoading) {
                                viewModel.handleIntent(intent = Intent.Submit)
                            }
                        },
                        containerColor = colorResource(id = R.color.black),
                        contentColor = colorResource(id = R.color.white),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 20.dp)
                            .height(56.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        shape = ButtonDefaults.elevatedShape as RoundedCornerShape,
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = colorResource(id = R.color.white),
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .height(24.dp)
                            )
                        } else {
                            Text(
                                text = "Entrar",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(weight = 1f)
                                .height(height = 1.dp),
                            thickness = DividerDefaults.Thickness,
                            color = Color.Gray
                        )

                        Text(
                            text = "O continua con",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 13.sp
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .weight(weight = 1f)
                                .height(height = 1.dp),
                            thickness = DividerDefaults.Thickness,
                            color = Color.Gray
                        )
                    }

                    Column {
                        // Google Buttom
                        BottomComp(
                            onButtonAction = { },
                            containerColor = colorResource(id = R.color.white),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
                            contentColor = colorResource(id = R.color.black),
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 6.dp, end = 6.dp, top = 9.dp)
                                .height(height = 48.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 56.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Iniciar con Google",
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Row(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .padding(start = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_google),
                                        contentDescription = "Google logo",
                                        modifier = Modifier.size(size = 24.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(height = 6.dp))

                        // Boton de Apple
                        BottomComp(
                            onButtonAction = { },
                            containerColor = colorResource(id = R.color.white),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
                            contentColor = colorResource(id = R.color.black),
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 6.dp, end = 6.dp, bottom = 9.dp)
                                .height(height = 48.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 56.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Iniciar con Apple",
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Row(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .padding(start = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_apple),
                                        contentDescription = "Apple logo",
                                        modifier = Modifier.size(size = 24.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }
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
}
