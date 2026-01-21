package com.clay.ecommerce_compose.ui.components.auth.registerTextFields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

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