package com.clay.ecommerce_compose.ui.components.admin.business

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.domain.model.Business

@Composable
fun BusinessCard(
    business: Business,
    onEdit: () -> Unit,
    onSuspend: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(weight = 1f)) {
                    Surface(
                        color = Color(color = 0xFF27AE60).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(size = 8.dp),
                        modifier = Modifier.size(size = 40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = null,
                                tint = Color(color = 0xFF27AE60),
                                modifier = Modifier.size(size = 22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(width = 12.dp))

                    Column {
                        Text(
                            text = business.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(color = 0xFF2C3E50)
                        )
                        Text(
                            text = "RNC: ${business.rnc}",
                            fontSize = 12.sp,
                            color = Color(color = 0xFF7F8C8D)
                        )
                    }
                }

                StatusBadge(
                    text = business.status,
                    status = when (business.status) {
                        "Aprobado" -> "success"
                        "Pendiente" -> "warning"
                        else -> "danger"
                    }
                )
            }

            Spacer(modifier = Modifier.height(height = 8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(color = 0xFF95A5A6),
                    modifier = Modifier.size(size = 16.dp)
                )
                Spacer(modifier = Modifier.width(width = 6.dp))
                Text(
                    text = business.address,
                    fontSize = 13.sp,
                    color = Color(color = 0xFF7F8C8D)
                )
            }

            Spacer(modifier = Modifier.height(height = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(weight = 1f),
                    shape = RoundedCornerShape(size = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(size = 18.dp)
                    )
                    Spacer(modifier = Modifier.width(width = 6.dp))
                    Text(text = "Editar")
                }

                OutlinedButton(
                    onClick = onSuspend,
                    modifier = Modifier.weight(weight = 1f),
                    shape = RoundedCornerShape(size = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(color = 0xFFF39C12)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(width = 6.dp))
                    Text(text = "Suspender")
                }
            }
        }
    }
}

@Composable
fun PendingBusinessCard(
    business: Business,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(color = 0xFFFFF3CD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = business.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF856404)
                    )
                    Text(
                        text = "RNC: ${business.rnc}",
                        fontSize = 12.sp,
                        color = Color(0xFF856404)
                    )
                    Text(
                        text = business.address,
                        fontSize = 12.sp,
                        color = Color(0xFF856404)
                    )
                }

                StatusBadge(text = "PENDIENTE", status = "warning")
            }

            Spacer(modifier = Modifier.height(height = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(weight = 1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(color = 0xFF27AE60)
                    ),
                    shape = RoundedCornerShape(size = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(width = 6.dp))
                    Text(text = "Aprobar")
                }

                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(weight = 1f),
                    shape = RoundedCornerShape(size = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(color = 0xFFE74C3C)
                    )
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(width = 6.dp))
                    Text(text = "Rechazar")
                }
            }
        }
    }
}
