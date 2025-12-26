package com.clay.ecommerce_compose.ui.screens.register


import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.auth.users.BottomComp
import kotlinx.coroutines.delay


@Composable
fun RegisterTextFields(
    email: String,
    onEmailChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    lastname: String,
    onLasNameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordChange: () -> Unit
) {
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = colorResource(id = R.color.white),
        unfocusedContainerColor = colorResource(id = R.color.white),
        disabledContainerColor = colorResource(id = R.color.white),

        focusedLabelColor = colorResource(id = R.color.focusedPurple),
        unfocusedLabelColor = Color.Gray,

        unfocusedBorderColor = Color.LightGray,
        focusedBorderColor = colorResource(id = R.color.focusedPurple),

        focusedLeadingIconColor = colorResource(id = R.color.focusedPurple),
        unfocusedLeadingIconColor = Color.Gray
    )

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = {
            Text(text = "Email", fontSize = 12.sp, style = MaterialTheme.typography.labelSmall)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
        },
        colors = colors,
        maxLines = 1,
        shape = RoundedCornerShape(size = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
    )

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(text = "Contraseña", fontSize = 12.sp, style = MaterialTheme.typography.labelSmall)
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
        colors = colors,
        shape = RoundedCornerShape(size = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
    )

    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = {
            Text(text = "Nombre", fontSize = 12.sp, style = MaterialTheme.typography.labelSmall)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Person, contentDescription = null)
        },
        maxLines = 1,
        colors = colors,
        shape = RoundedCornerShape(size = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
    )

    OutlinedTextField(
        value = lastname,
        onValueChange = onLasNameChange,
        label = {
            Text(text = "Apellido", fontSize = 12.sp, style = MaterialTheme.typography.labelSmall)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person3,
                contentDescription = null
            )
        },
        maxLines = 1,
        colors = colors,
        shape = RoundedCornerShape(size = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
    )
}


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavHostController,
) {

    var showPassword by remember { mutableStateOf(value = false) }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error, state.registered) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        if (state.registered) {
            Toast.makeText(context, "Registrado ${state.name}", Toast.LENGTH_SHORT).show()
            delay(300)
            navController.navigate(route = "login")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.grey))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp, horizontal = 16.dp)
            ) {
                IconButton(onClick = { navController.navigate(route = "login") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(size = 28.dp)
                    )
                }

                Text(
                    text = "Registrarse",
                    fontSize = 38.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth()
            ) {
                RegisterTextFields(
                    email = state.email,
                    onEmailChange = {
                        viewModel.handleIntent(intent = Intent.EmailChanged(email = it))
                    },
                    password = state.password,
                    onPasswordChange = {
                        viewModel.handleIntent(
                            intent = Intent.PasswordChanged(
                                password = it
                            )
                        )
                    },
                    name = state.name,
                    onNameChange = {
                        viewModel.handleIntent(
                            intent = Intent.NameChanged(
                                name = it
                            )
                        )
                    },
                    lastname = state.lastname,
                    onLasNameChange = {
                        viewModel.handleIntent(
                            intent = Intent.LastNameChanged(
                                lastname = it
                            )
                        )
                    },
                    showPassword = showPassword,
                    onShowPasswordChange = {
                        showPassword = !showPassword
                    }
                )

                BottomComp(
                    onButtonAction = {
                        if (!state.isLoading) {
                            viewModel.handleIntent(
                                intent = Intent.Submit
                            )
                        }
                    },
                    containerColor = colorResource(id = R.color.tintRed),
                    contentColor = colorResource(id = R.color.white),
                    shape = CircleShape,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 7.dp)
                        .height(height = 54.dp),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.white),
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .padding(all = 6.dp)
                                .height(height = 20.dp)
                        )
                    } else {
                        Text(
                            text = "Registrate",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(height = 120.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tienes un negocio")
                Text(text = "Registralo aqui", color = colorResource(id = R.color.focusedPurple), modifier = Modifier.clickable {
                    navController.navigate(route = "registerBusiness")
                })
                Spacer(modifier = Modifier.height(height = 20.dp))
            }
        }
    }
}
