package com.clay.ecommerce_compose.ui.screens.businesess

sealed class BusinessAccountProductIntent {
    data class SetBusinessId(val id: String) : BusinessAccountProductIntent()
    data class ProductName(val name: String) : BusinessAccountProductIntent()
    data class ProductImage(val imgUrl: String) : BusinessAccountProductIntent()
    data class ProductDescription(val description: String) : BusinessAccountProductIntent()
    data class ProductPrice(val price: String) : BusinessAccountProductIntent()
    data class ProductStock(val stock: String) : BusinessAccountProductIntent()

    data class SetCategory(val categoryId: Int?) : BusinessAccountProductIntent()
    data class SetMinStock(val minStock: Int) : BusinessAccountProductIntent()
    data class SetMaxStock(val maxStock: Int?) : BusinessAccountProductIntent()
    data class SetStockAlertEnabled(val enabled: Boolean) : BusinessAccountProductIntent()

    object ProductActive : BusinessAccountProductIntent()
    object ProductInactive : BusinessAccountProductIntent()

    object DisableProduct : BusinessAccountProductIntent()
    object AddProduct : BusinessAccountProductIntent()
    object UpdateProduct : BusinessAccountProductIntent()
}
