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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
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
    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = {
            Text(text = "Email", style = MaterialTheme.typography.labelSmall)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
    )

    TextField(
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
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
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
        shape = RoundedCornerShape(size = 10.dp),
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
            Toast.makeText(context, "Bienvenido ${state.currentUser}", Toast.LENGTH_SHORT).show()
            delay(300)
            navController.navigate(route = "userHome")
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
                .height(height = 450.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(all = 16.dp)
        ) {
            Text(
                text = "Bienvenido",
                fontSize = 32.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(id = R.string.logueate),
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(all = 20.dp)
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Aun no tienes una cuenta?",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .clickable { navController.navigate(route = "register") }
                        .align(Alignment.CenterStart)
                )
            }

            // login Buttom
            BottomComp(
                onButtonAction = {
                    viewModel.handleIntent(intent = LoginViewModel.Intent.Submit)
                },
                containerColor = Color(color = 0xff3ddb84),
                contentColor = Color.White,
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 18.dp),
            ) {
                Text(
                    text = "Entrar",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                    text = "Puedes continuar con",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 14.sp
                )

                HorizontalDivider(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 1.dp),
                    thickness = DividerDefaults.Thickness,
                    color = Color.Gray
                )
            }

            // Google Buttom
            BottomComp(
                onButtonAction = { viewModel.handleIntent(intent = LoginViewModel.Intent.Submit) },
                containerColor = Color.White,
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
                contentColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 6.dp)
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
                        style = MaterialTheme.typography.labelMedium
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
        }
    }
}
