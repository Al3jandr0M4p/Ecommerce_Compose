package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.ui.screens.client.cart.coupon.Coupon
import com.clay.ecommerce_compose.utils.helpers.formatPrice

@Composable
fun CouponInput(
    appliedCoupon: Coupon?,
    couponError: String?,
    couponDiscount: Double,
    onApplyCoupon: (String) -> Unit,
    onRemoveCoupon: () -> Unit,
    modifier: Modifier = Modifier
) {
    var couponCode by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (appliedCoupon == null) {
            // Banner tipo speech bubble
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Banner con pico/flecha
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
                            .background(Color(0xFFE53935))
                            .drawBehind {
                                // Dibujar el triángulo/pico apuntando hacia abajo
                                val trianglePath = Path().apply {
                                    val triangleWidth = 40f
                                    val triangleHeight = 20f
                                    val startX = 40f // Posición desde la izquierda

                                    moveTo(startX, size.height)
                                    lineTo(startX + triangleWidth / 2, size.height + triangleHeight)
                                    lineTo(startX + triangleWidth, size.height)
                                    close()
                                }
                                drawPath(
                                    path = trianglePath,
                                    color = Color(0xFFE53935)
                                )
                            }
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEB3B)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎊",
                                    fontSize = 18.sp
                                )
                            }
                            Text(
                                text = "No te pierdas nuestras promos",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Input debajo del banner
                    OutlinedTextField(
                        value = couponCode,
                        onValueChange = { couponCode = it.uppercase() },
                        placeholder = {
                            Text(
                                text = "Ingresa un cupon de descuento",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.LightGray,
                            errorIndicatorColor = Color.Red,
                            errorContainerColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (couponCode.isNotBlank()) {
                                    onApplyCoupon(couponCode)
                                }
                            }
                        ),
                        isError = couponError != null
                    )

                    // Mensaje de error
                    if (couponError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = couponError,
                            color = Color.Red,
                            fontSize = 13.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            // Cupón aplicado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cupón aplicado: ${appliedCoupon.code}",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Ahorro: -RD$${formatPrice(couponDiscount)}",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2E7D32)
                    )
                }

                IconButton(onClick = onRemoveCoupon) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remover cupón",
                        tint = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}
