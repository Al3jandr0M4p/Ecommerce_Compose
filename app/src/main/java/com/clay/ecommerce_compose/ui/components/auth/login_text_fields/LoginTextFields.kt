package com.clay.ecommerce_compose.ui.components.auth.login_text_fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
