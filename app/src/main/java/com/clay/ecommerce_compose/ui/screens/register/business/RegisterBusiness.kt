package com.clay.ecommerce_compose.ui.screens.register.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.auth.business.ImageInput
import com.clay.ecommerce_compose.ui.components.auth.business.TimePickerComponent

@Composable
fun Step1Content(
    state: RegisterBusinessState,
    onIntent: (Intent) -> Unit,
    showPassword: Boolean,
    onShowPassword: () -> Unit,
    modifier: Modifier = Modifier
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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        item {
            OutlinedTextField(
                value = state.email,
                onValueChange = { onIntent(Intent.EmailChanged(email = it)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Email del negocio",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = colors,
                maxLines = 1,
                shape = RoundedCornerShape(size = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
        item {
            OutlinedTextField(
                value = state.password,
                onValueChange = { onIntent(Intent.PasswordChanged(password = it)) },
                label = {
                    Text(
                        text = "Contraseña",
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
                    IconButton(onClick = onShowPassword) {
                        val visibilityIcon =
                            if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            imageVector =
                                visibilityIcon,
                            contentDescription = if (showPassword) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                colors = colors,
                maxLines = 1,
                shape = RoundedCornerShape(size = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
        item {
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onIntent(
                        Intent.NameChanged(name = it)
                    )
                },
                label = {
                    Text(
                        text = "Nombre del Negocio",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null
                    )
                },
                colors = colors,
                maxLines = 1,
                shape = RoundedCornerShape(size = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
        item {
            OutlinedTextField(
                value = state.category,
                onValueChange = {
                    onIntent(
                        Intent.CategoryChanged(category = it)
                    )
                },
                label = {
                    Text(
                        text = "Categoria",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null
                    )
                },
                colors = colors,
                maxLines = 1,
                shape = RoundedCornerShape(size = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }

        item {
            OutlinedTextField(
                value = state.telefono,
                onValueChange = { onIntent(Intent.TelefonoChanged(telefono = it)) },
                label = {
                    Text(
                        text = "Telefono",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null
                    )
                },
                colors = colors,
                maxLines = 1,
                shape = RoundedCornerShape(size = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }

        item {
            // RNC
            OutlinedTextField(
                value = state.rnc,
                onValueChange = {
                    if (it.length <= 9 && it.all { char -> char.isDigit() }) {
                        onIntent(Intent.RncChanged(rnc = it))
                    }
                },
                label = {
                    Text(
                        text = "RNC (9 dígitos)",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null
                    )
                },
                colors = colors,
                maxLines = 1,
                shape = RoundedCornerShape(size = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }

        item {
            // Campo NCF
            OutlinedTextField(
                value = state.ncf,
                onValueChange = {
                    val filtered = it.uppercase().take(11)
                    if (filtered.isEmpty() ||
                        (filtered.startsWith("B") && filtered.drop(1)
                            .all { char -> char.isDigit() })
                    ) {
                        onIntent(Intent.NcfChanged(ncf = filtered))
                    }
                },
                label = {
                    Text(
                        text = "NCF (B + 10 dígitos)",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null
                    )
                },
                colors = colors,
                maxLines = 1,
                shape = RoundedCornerShape(size = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                placeholder = {
                    Text(
                        text = "B0000000000",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimePickerComponent(
                    label = "Hora de entrada"
                ) { time -> onIntent(Intent.HorarioAperturaChanged(time)) }
                TimePickerComponent(
                    label =
                        "Hora de salida"
                ) { time -> onIntent(Intent.HorarioCierreChanged(time)) }
            }
        }

        item {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp)
            ) {
                ImageInput { uri ->
                    onIntent(
                        Intent.LogoChanged(logo = uri)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "¿Ofreces delivery?",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall
                )
                Switch(
                    checked = state.hasDelivery,
                    onCheckedChange = {
                        onIntent(
                            Intent.HasDeliveryChanged(
                                hasDelivery = it
                            )
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = R.color.focusedPurple),
                        checkedTrackColor = colorResource(id = R.color.focusedPurple).copy(alpha = 0.5f)
                    )
                )
            }
        }
        item {
            Button(
                onClick =
                    { onIntent(Intent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(height = 56.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(id = R.color.white),
                    containerColor = colorResource(id = R.color.black),
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Finalizar Registro")
                }
            }
        }
        item {
            if (state.error != null) {
                Text(
                    text = state.error,
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}


@Composable
fun RegisterBusiness(viewModel: RegisterBusinessViewModel, navController: NavHostController) {
    var showPassword by remember { mutableStateOf(value = true) }
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.isRegistrationSuccessful) {
        if (state.isRegistrationSuccessful) {
            navController.navigate(route = "businessHome/${state.businessId}") {
                popUpTo(route = "registerBusiness") {
                    inclusive = true
                }
            }
        }
    }
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.white))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 19.dp)
        ) {
            IconButton(
                onClick = { navController.navigate(route = "register") },
                modifier = Modifier.size(size = 62.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                )
            }
            Text(
                text =
                    "Negocios", fontSize = 44.sp, style = MaterialTheme.typography.labelSmall
            )
        }

        Step1Content(
            state = state,
            onIntent = viewModel::handleIntent,
            showPassword = showPassword,
            onShowPassword = { showPassword = !showPassword },
            modifier = Modifier
        )
    }
}
