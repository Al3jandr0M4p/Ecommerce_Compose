package com.clay.ecommerce_compose.ui.screens.register

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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

    val focusedPurple = Color(color = 0xFF6200EE)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(
                    text = "Email",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    },
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
                    maxLines = 1,
                    shape = RoundedCornerShape(size = 16.dp),
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(
                    text = "Password",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
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
                )
            }
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(
                    text = "Nombre",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
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
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(
                    text = "apellido",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall
                )

                OutlinedTextField(
                    value = lastname,
                    onValueChange = onLasNameChange,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person3,
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
                )
            }
        }
    }

}


@Composable
fun BottomComp(
    modifier: Modifier = Modifier,
    onButtonAction: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    shape: RoundedCornerShape,
    elevation: ButtonElevation,
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
        shape = shape,
        content = content
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp)
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
                    .padding(all = 20.dp)
                    .fillMaxWidth()
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
                    name = state.name,
                    onNameChange = {
                        viewModel.handleIntent(
                            intent = RegisterViewModel.Intent.NameChanged(
                                name = it
                            )
                        )
                    },
                    lastname = state.lastname,
                    onLasNameChange = {
                        viewModel.handleIntent(
                            intent = RegisterViewModel.Intent.LastNameChanged(
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
                        viewModel.handleIntent(
                            intent = RegisterViewModel.Intent.Submit
                        )
                    },
                    containerColor = Color(color = 0xffd91600),
                    contentColor = Color.White,
                    shape = CircleShape,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 6.dp)
                        .height(height = 56.dp),
                ) {
                    Text(
                        text = "Registrate",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black, shape = RoundedCornerShape(size = 16.dp))
                    .padding(all = 18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    modifier = Modifier.size(size = 26.dp),
                    tint = Color.White
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
                        color = Color.White
                    )
                    Text(
                        text = "Registra y administra tu negocio de la mejor forma aqui",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(size = 20.dp),
                    tint = Color.White
                )
            }
        }
    }
}