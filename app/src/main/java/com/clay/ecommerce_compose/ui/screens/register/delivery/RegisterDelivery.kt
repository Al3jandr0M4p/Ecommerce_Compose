package com.clay.ecommerce_compose.ui.screens.register.delivery

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun DeliveryRegisterTextFields(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDelivery(navController: NavHostController, viewModel: RegisterDeliveryViewModel) {
    val state by viewModel.state.collectAsState()
    var showPassword by remember { mutableStateOf(value = false) }
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Delivery",
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(route = "login") },
                        modifier = Modifier.Companion.size(size = 32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            item {
                DeliveryRegisterTextFields(
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
            }
        }
    }
}
