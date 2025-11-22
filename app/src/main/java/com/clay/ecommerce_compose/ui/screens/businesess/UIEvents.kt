package com.clay.ecommerce_compose.ui.screens.businesess

sealed class UIEvents {
    data class ShowMessage(val message: String): UIEvents()
    object CloseSheet : UIEvents()
}
