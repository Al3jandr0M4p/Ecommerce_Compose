package com.clay.ecommerce_compose.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ===== MODELOS ACTUALIZADOS =====

@Serializable
data class ProductInsertPayload(
    @SerialName("business_id")
    val businessId: Int,
    val name: String,
    val description: String? = null,
    val price: Double,
    val stock: Int,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("is_active")
    val isActive: Boolean = true,
    // Nuevos campos para categorías y stock
    @SerialName("category_id")
    val categoryId: Int? = null,
    @SerialName("min_stock")
    val minStock: Int = 5,
    @SerialName("max_stock")
    val maxStock: Int? = null,
    @SerialName("stock_alert_enabled")
    val stockAlertEnabled: Boolean = true
)

@Serializable
data class ProductPayload(
    val id: Int? = null,
    val name: String,
    val description: String? = null,
    val price: Double,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val stock: Int,
    @SerialName("business_id")
    val businessId: Int,
    // Nuevos campos
    @SerialName("category_id")
    val categoryId: Int? = null,
    @SerialName("min_stock")
    val minStock: Int = 5,
    @SerialName("max_stock")
    val maxStock: Int? = null,
    @SerialName("stock_alert_enabled")
    val stockAlertEnabled: Boolean = true,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

// ===== NUEVOS MODELOS =====

@Serializable
data class ProductCategory(
    val id: Int? = null,
    @SerialName("business_id")
    val businessId: Int,
    val name: String,
    val description: String? = null,
    @SerialName("parent_category_id")
    val parentCategoryId: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class StockMovement(
    val id: Int? = null,
    @SerialName("product_id")
    val productId: Int,
    @SerialName("movement_type")
    val movementType: StockMovementType,
    val quantity: Int,
    @SerialName("previous_stock")
    val previousStock: Int,
    @SerialName("new_stock")
    val newStock: Int,
    @SerialName("reference_id")
    val referenceId: String? = null,
    val notes: String? = null,
    @SerialName("created_by")
    val createdBy: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
enum class StockMovementType {
    @SerialName("sale")
    SALE,
    @SerialName("restock")
    RESTOCK,
    @SerialName("adjustment")
    ADJUSTMENT,
    @SerialName("return")
    RETURN
}

@Serializable
data class LowStockProduct(
    val id: Int,
    @SerialName("business_id")
    val businessId: Int,
    val name: String,
    @SerialName("current_stock")
    val currentStock: Int,
    @SerialName("min_stock")
    val minStock: Int,
    val price: Double,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("business_name")
    val businessName: String,
    @SerialName("owner_id")
    val ownerId: String
)

@Serializable
data class ProductsByCategory(
    @SerialName("category_id")
    val categoryId: Int,
    @SerialName("category_name")
    val categoryName: String,
    @SerialName("product_count")
    val productCount: Int,
    val products: List<ProductPayload>
)

@Serializable
data class CategoryRequest(
    @SerialName("business_id")
    val businessId: Int,
    val name: String,
    val description: String? = null,
    @SerialName("parent_category_id")
    val parentCategoryId: Int? = null
)

@Serializable
data class StockAdjustmentRequest(
    @SerialName("product_id")
    val productId: Int,
    val quantity: Int,
    @SerialName("movement_type")
    val movementType: StockMovementType,
    val notes: String? = null
)