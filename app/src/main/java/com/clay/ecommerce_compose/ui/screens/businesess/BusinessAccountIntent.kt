package com.clay.ecommerce_compose.ui.screens.businesess

sealed class BusinessAccountProductIntent {
    data class SetBusinessId(val id: String) : BusinessAccountProductIntent()
    data class ProductName(val name: String) : BusinessAccountProductIntent()
    data class ProductImage(val imgUrl: String) : BusinessAccountProductIntent()
    data class ProductDescription(val description: String) : BusinessAccountProductIntent()
    data class ProductPrice(val price: String) : BusinessAccountProductIntent()
    data class ProductStock(val stock: String) : BusinessAccountProductIntent()
    data class SetProductStockControl(val has: Boolean) : BusinessAccountProductIntent()

    object ProductActive : BusinessAccountProductIntent()
    object ProductInactive : BusinessAccountProductIntent()

    object DisableProduct : BusinessAccountProductIntent()
    object AddProduct : BusinessAccountProductIntent()
    object UpdateProduct : BusinessAccountProductIntent()
}
