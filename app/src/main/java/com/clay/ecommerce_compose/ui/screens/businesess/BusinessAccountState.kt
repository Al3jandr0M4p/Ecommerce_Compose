data class BusinessAccountProductState(
    val id: Int? = null,
    val name: String = "",
    val businessId: String = "",
    val imgUrl: String = "",
    val description: String? = null,
    val price: String = "",

    // Control de stock
    val hasStockControl: Boolean = true, // Por defecto activado
    val stock: String = "",

    // Nuevos campos para categorías y gestión avanzada de stock
    val categoryId: Int? = null,
    val minStock: Int = 5,
    val maxStock: Int? = null,
    val stockAlertEnabled: Boolean = true,

    val isActive: Boolean = true,

    // Errores
    val nameError: String? = null,
    val priceError: String? = null,
    val stockError: String? = null,
    val descriptionError: String? = null
)
