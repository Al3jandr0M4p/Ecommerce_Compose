package com.clay.ecommerce_compose.ui.screens.admin.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.clay.ecommerce_compose.ui.components.admin.bars.BusinessTopBar
import com.clay.ecommerce_compose.ui.components.admin.business.AllBusinessesList
import com.clay.ecommerce_compose.ui.components.admin.business.PendingBusinessesList
import com.clay.ecommerce_compose.ui.components.admin.dialog.AddBusinessDialog
import com.clay.ecommerce_compose.ui.components.admin.dialog.EditBusinessDialog
import com.clay.ecommerce_compose.ui.components.admin.tabs.BusinessTabs
import com.clay.ecommerce_compose.utils.hooks.useBusinessAdminScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessScreen(
    onBack: () -> Unit
) {
    val businessAdmin = useBusinessAdminScreen()

    Scaffold(
        topBar = {
            BusinessTopBar(onBack = onBack, pendingCount = businessAdmin.pendingBusinesses.size)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
                .background(Color(color = 0xFFF5F6FA))
        ) {
            BusinessTabs(
                selectedTab = businessAdmin.selectedTab,
                onTabChange = businessAdmin.setSelectedTab,
                pendingCount = businessAdmin.pendingBusinesses.size
            )

            when (businessAdmin.selectedTab) {
                0 -> AllBusinessesList(
                    businesses = businessAdmin.businesses,
                    onEdit = {
                        businessAdmin.setSelectedBusiness(it)
                        businessAdmin.setShowEditDialog(true)
                    },
                    onSuspend = { businessAdmin.suspend },
                    onAdd = { businessAdmin.setShowAddDialog(true) }
                )

                1 -> PendingBusinessesList(
                    businesses = businessAdmin.pendingBusinesses,
                    onApprove = businessAdmin.approve,
                    onReject = businessAdmin.reject
                )
            }
        }
    }

    if (businessAdmin.showEditDialog && businessAdmin.selectedBusiness != null) {
        EditBusinessDialog(
            business = businessAdmin.selectedBusiness,
            onDismiss = {
                businessAdmin.setShowEditDialog(false)
                businessAdmin.setSelectedBusiness(null)
            },
            onConfirm = { name, rnc, address ->
                businessAdmin.updateBusiness(
                    businessAdmin.selectedBusiness,
                    name,
                    rnc,
                    address
                )
                businessAdmin.setShowEditDialog(false)
                businessAdmin.setSelectedBusiness(null)
            }
        )
    }

    if (businessAdmin.showAddDialog) {
        AddBusinessDialog(
            onDismiss = { businessAdmin.setShowAddDialog(false) },
            onConfirm = { name, rnc, address ->
                businessAdmin.addBusiness(name, rnc, address)
                businessAdmin.setShowAddDialog(false)
            }
        )
    }
}
