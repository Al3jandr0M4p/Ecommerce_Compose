//package com.clay.ecommerce_compose.utils.hooks
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import com.clay.ecommerce_compose.domain.model.Business
//import java.util.UUID
//
//data class BusinessAdminController(
//    val businesses: List<Business>,
//    val pendingBusinesses: List<Business>,
//
//    val selectedTab: Int,
//    val setSelectedTab: (Int) -> Unit,
//
//    val showEditDialog: Boolean,
//    val setShowEditDialog: (Boolean) -> Unit,
//
//    val showAddDialog: Boolean,
//    val setShowAddDialog: (Boolean) -> Unit,
//
//    val selectedBusiness: Business?,
//    val setSelectedBusiness: (Business?) -> Unit,
//
//    val addBusiness: (String, String, String) -> Unit,
//    val updateBusiness: (Business, String, String, String) -> Unit,
//    val approve: (Business) -> Unit,
//    val reject: (Business) -> Unit,
//    val suspend: (Business) -> Unit
//)
//
//@Composable
//fun useBusinessAdminScreen(): BusinessAdminController {
//    val businesses = remember { mutableStateListOf<Business>() }
//    var selectedTab by remember { mutableIntStateOf(value = 0) }
//    var showEditDialog by remember { mutableStateOf(value = false) }
//    var showAddDialog by remember { mutableStateOf(value = false) }
//    var selectedBusiness by remember { mutableStateOf<Business?>(value = null) }
//
//    val pending = businesses.filter { it.status == "Pendiente" }
//
//    fun addBusiness(name: String, rnc: String, address: String) {
//        businesses.add(
//            Business(
//                id = UUID.randomUUID().toString(),
//                name = name,
//                rnc = rnc,
//                address = address,
//                status = "Aprobado"
//            )
//        )
//    }
//
//    fun updateBusiness(business: Business, name: String, rnc: String, address: String) {
//        val index = businesses.indexOf(business)
//
//        if (index >= 0) {
//            businesses[index] = business.copy(
//                name = name,
//                rnc = rnc,
//                address = address
//            )
//        }
//    }
//
//    fun approveBusiness(business: Business) {
//        val index = businesses.indexOf(business)
//        if (index >= 0) businesses[index] = business.copy(status = "Aprobado")
//    }
//
//    fun rejectBusiness(business: Business) {
//        businesses.remove(business)
//    }
//
//    fun suspendBusiness(business: Business) {
//        val index = businesses.indexOf(business)
//        if (index >= 0) businesses[index] = business.copy(status = "Suspendido")
//    }
//
//    return BusinessAdminController(
//        businesses = businesses,
//        pendingBusinesses = pending,
//        selectedTab = selectedTab,
//        setSelectedTab = { selectedTab = it },
//        showEditDialog = showEditDialog,
//        setShowEditDialog = { showEditDialog = it },
//        showAddDialog = showAddDialog,
//        setShowAddDialog = { showAddDialog = it },
//        selectedBusiness = selectedBusiness,
//        setSelectedBusiness = { selectedBusiness = it },
//
//        addBusiness = ::addBusiness,
//        updateBusiness = ::updateBusiness,
//        approve = ::approveBusiness,
//        reject = ::rejectBusiness,
//        suspend = ::suspendBusiness
//    )
//}
