package com.clay.ecommerce_compose.ui.screens.register

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.clay.ecommerce_compose.R
import kotlinx.coroutines.delay


@Composable
fun RegisterTextFields(
    email: String,
    onEmailChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordChange: () -> Unit
) {
    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = {
            Text(text = "Email")
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
        value = username,
        onValueChange = onUsernameChange,
        label = {
            Text(text = "Username")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Person, contentDescription = null)
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
            Text(text = "Password")
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
        modifier = Modifier.fillMaxWidth()
    )

}


@Composable
fun BottomComp(
    modifier: Modifier = Modifier,
    onButtonAction: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    shape: RoundedCornerShape,
    padding: PaddingValues,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onButtonAction,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
        shape = shape,
        contentPadding = padding,
        content = content
    )
}


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {

    var showPassword by remember { mutableStateOf(value = false) }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error, state.registered) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        if (state.registered) {
            Toast.makeText(context, "Registrado ${state.username}", Toast.LENGTH_SHORT).show()
            delay(300)
            navController.navigate(route = "login")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 400.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Registrate",
                fontSize = 30.sp,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
        ) {
            RegisterTextFields(
                email = state.email,
                onEmailChange = {
                    viewModel.handleIntent(intent = RegisterViewModel.Intent.EmailChanged(email = it))
                },
                password = state.password,
                onPasswordChange = {
                    viewModel.handleIntent(
                        intent = RegisterViewModel.Intent.PasswordChanged(
                            password = it
                        )
                    )
                },
                username = state.username,
                onUsernameChange = {
                    viewModel.handleIntent(
                        intent = RegisterViewModel.Intent.UsernameChanged(
                            username = it
                        )
                    )
                },
                showPassword = showPassword,
                onShowPasswordChange = {
                    showPassword = !showPassword
                }
            )

            BottomComp(
                onButtonAction = { viewModel.handleIntent(intent = RegisterViewModel.Intent.Submit) },
                containerColor = Color(color = 0xff3ddb84),
                contentColor = Color.White,
                shape = RoundedCornerShape(size = 10.dp),
                padding = PaddingValues(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 6.dp),
            ) {
                Text(
                    text = "Registrate",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 18.sp
                )
            }
        }
    }

}

