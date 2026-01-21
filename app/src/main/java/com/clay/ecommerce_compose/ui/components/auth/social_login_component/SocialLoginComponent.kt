package com.clay.ecommerce_compose.ui.components.auth.social_login_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R
import com.clay.ecommerce_compose.ui.components.auth.login_text_fields.LoginTextFields
import com.clay.ecommerce_compose.ui.components.auth.users.BottomComp
import com.clay.ecommerce_compose.ui.screens.login.Intent

@Composable
fun SocialLoginComponent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LoginTextFields(
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
            showPassword = showPassword,
            onShowPasswordChange = { showPassword = !showPassword }
        )

        // login Buttom
        BottomComp(
            onButtonAction = {
                if (!state.isLoading) {
                    viewModel.handleIntent(intent = Intent.Submit)
                }
            },
            containerColor = colorResource(id = R.color.black),
            contentColor = colorResource(id = R.color.white),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 20.dp)
                .height(56.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            shape = ButtonDefaults.elevatedShape as RoundedCornerShape,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.white),
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .padding(6.dp)
                        .height(24.dp)
                )
            } else {
                Text(
                    text = "Entrar",
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

            Spacer(modifier = Modifier.height(height = 6.dp))

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
    }
}