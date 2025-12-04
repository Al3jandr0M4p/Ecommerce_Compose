package com.clay.ecommerce_compose.ui.components.admin.business

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.clay.ecommerce_compose.domain.model.Business
import com.clay.ecommerce_compose.ui.screens.admin.business.PendingBusinessCard
import com.clay.ecommerce_compose.ui.screens.admin.users.EmptyState

@Composable
fun AllBusinessesList(
    businesses: List<Business>,
    onEdit: (Business) -> Unit,
    onSuspend: (Business) -> Unit,
    onAdd: () -> Unit
) {
    if (businesses.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Store,
            message = "No hay negocios registrados",
            actionText = "Agregar Negocio",
            onAction = onAdd
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            items(items = businesses) { business ->
                BusinessCard(
                    business = business,
                    onEdit = { onEdit(business) },
                    onSuspend = { onSuspend(business) }
                )
            }
        }
    }
}

@Composable
fun PendingBusinessesList(
    businesses: List<Business>,
    onApprove: (Business) -> Unit,
    onReject: (Business) -> Unit
) {
    if (businesses.isEmpty()) {
        EmptyState(
            icon = Icons.Default.CheckCircle,
            message = "No hay solicitudes pendientes",
            actionText = "",
            onAction = { }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            items(items = businesses) { business ->
                PendingBusinessCard(
                    business = business,
                    onApprove = { onApprove(business) },
                    onReject = { onReject(business) }
                )
            }
        }
    }
}
