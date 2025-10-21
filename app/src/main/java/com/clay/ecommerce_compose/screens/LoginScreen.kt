package com.clay.ecommerce_compose.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

/**
 *
 * Composable `HeaderComp` para renderizar la imagen/logo de fondo de la pantalla del login
 *
 * @param imgUrl property de tipo `Int` para asignar la imagen a mostrar
 * @param descriptionContent property de tipo `String` para asignar la descripcion de la imagen
 * @param modifier property de la clase `Modifier` para personalizar la apariencia del composable
 *
 * @author Alejandro
 *
 * */
@Composable
fun HeaderComp(imgUrl: Int, descriptionContent: String, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = imgUrl),
        contentDescription = descriptionContent,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

/**
 *
 * Composable `BottomComp` para renderizar el boton del login
 *
 * @param modifier property de la clase `Modifier` para personalizar la apariencia del composable
 * @param loginButtonAction callback para manejar el boton del login
 *
 * @author Alejandro
 *
 * */
@Composable
fun BottomComp(modifier: Modifier = Modifier, loginButtonAction: () -> Unit) {
    Button(
        onClick = loginButtonAction,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(color = 0xff3ddb84),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(size = 10.dp),
        contentPadding = PaddingValues()
    ) {
        Text(text = "Iniciar Sesion", fontSize = 18.sp)
    }
}


/**
 *
 * Composable `LoginTextFields` para poder renderizar los campos de texto del login
 *
 * @param email property de tipo `String` para asignar el email del usuario
 * @param onEmailChange callback para cambiar el email del usuario
 * @param password property de tipo `String` para asignar la contraseña del usuario
 * @param onPasswordChange callback para cambiar la contraseña del usuario
 * @param showPassword property de tipo `Boolean` para mostrar o no la contraseña del usuario
 * @param onShowPasswordChange callback para cambiar el `showPassword` para el estado del boton
 *
 * @author Alejandro
 *
 * */
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

/**
 *
 * Composable `LoginScreen` para renderizar la pantalla del loginScreen
 *
 * @param modifier property de tipo `Modifier` para personalizar la apariencia del composable
 *
 * @author Alejandro
 *
 * */
@Composable
fun LoginScreen(modifier: Modifier = Modifier, loginButtonAction: () -> Unit = {}) {
    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }
    var showPassword by remember { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        HeaderComp(
            imgUrl = R.drawable.ic_launcher_background,
            descriptionContent = "Logo",
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
                text = "Bienvenido,",
                fontSize = 30.sp,
                fontFamily = FontFamily.Default,
            )
            Text(
                text = stringResource(id = R.string.logueate),
                fontSize = 20.sp,
                fontFamily = FontFamily.Default
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
        ) {
            LoginTextFields(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                showPassword = showPassword,
                onShowPasswordChange = { showPassword = !showPassword }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {}) {
                    Text(
                        text = painterResource(id = R.string.any_account),
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
                TextButton(onClick = {}) {
                    Text(text = painterResource(id = R.string.forget_password), color = Color.Gray, fontSize = 14.sp)
                }
            }

            BottomComp(
                loginButtonAction = loginButtonAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 6.dp),
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
