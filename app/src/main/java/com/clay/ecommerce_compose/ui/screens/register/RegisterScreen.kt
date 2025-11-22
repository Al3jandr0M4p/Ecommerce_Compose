package com.clay.ecommerce_compose.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
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
import androidx.compose.ui.draw.shadow
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
                Spacer(modifier = Modifier.height(height = 120.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 60.dp, start = 16.dp, end = 16.dp)
                .clickable {
                    navController.navigate(route = "registerBusiness")
                }
                .shadow(elevation = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.black),
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .padding(all = 26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    modifier = Modifier.size(size = 26.dp),
                    tint = colorResource(id = R.color.white)
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(weight = 1f)
                ) {
                    Text(
                        text = "Tienes un negocio? Registralo",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.white)
                    )
                    Text(
                        text = "Registra y administra tu negocio de la mejor forma aqui",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.white)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(size = 20.dp),
                    tint = colorResource(id = R.color.white)
                )
            }
        }
    }
}
