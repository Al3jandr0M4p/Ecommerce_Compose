package com.clay.ecommerce_compose.data.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.clay.ecommerce_compose.domain.model.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.storage

class BusinessRepository(
    private val supabase: SupabaseClient,
    private val context: Context?
) {

    // ===== MÉTODOS EXISTENTES =====

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun uploadProductImage(imageUri: String): Result<String> {
        return try {
            val uri = imageUri.toUri()

            val byteArray = context?.contentResolver?.openInputStream(uri).use {
                it?.readBytes()
            } ?: throw Exception("No se pudo leer el archivo de imagen")

            val filename = "${System.currentTimeMillis()}.jpg"

            supabase.storage.from("product-image").upload(filename, byteArray)

            val publicUrl = supabase.storage.from("product-image").publicUrl(filename)

            Result.success(publicUrl)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al subir imagen: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getBusinessInfoByID(id: Int?): BusinessProfile? {
        val businessResult = supabase.from("businesses")
            .select {
                filter { id?.let { id -> eq("id", id) } }
            }
            .decodeSingleOrNull<BusinessProfile>()

        return businessResult
    }

    suspend fun getProductsByBusinessId(businessId: Int?): List<ProductPayload> {
        Log.d("BusinessRepository", "getProductsByBusinessId: $businessId")

        val result = supabase.from("products")
            .select {
                filter {
                    businessId?.let { id ->
                        Log.d("BusinessRepository", "Aplicando filtro con id: $id")
                        eq("business_id", id)
                    }
                }
            }
            .decodeList<ProductPayload>()

        Log.d("BusinessRepository", "Productos encontrados: ${result.size}")

        return result
    }

    suspend fun getBusinessById(businessId: String): BusinessProfile? {
        return supabase.from("businesses")
            .select {
                filter { eq("id", businessId) }
            }
            .decodeSingleOrNull()
    }

    suspend fun addProduct(product: ProductInsertPayload): Result<Unit> {
        return try {
            supabase.from("products").insert(product)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al agregar producto: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: Int?, updates: ProductPayload): Result<Unit> {
        return try {
            if (productId == null) {
                return Result.failure(Exception("Product ID no puede ser null"))
            }

            supabase.from("products").update(updates) {
                filter { eq("id", productId) }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al actualizar producto: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun disableProduct(productId: Int): Result<Unit> {
        return try {
            supabase.from("products").update(mapOf("is_active" to false)) {
                filter { eq("id", productId) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al desactivar producto: ${e.message}")
            Result.failure(e)
        }
    }

    // ===== NUEVOS MÉTODOS PARA CATEGORÍAS =====

    suspend fun getCategories(businessId: Int): Result<List<ProductCategory>> {
        return try {
            val categories = supabase.from("product_categories")
                .select {
                    filter { eq("business_id", businessId) }
                }
                .decodeList<ProductCategory>()

            Result.success(categories)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al obtener categorías: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun createCategory(category: CategoryRequest): Result<ProductCategory> {
        return try {
            val result = supabase.from("product_categories")
                .insert(category) {
                    select()
                }
                .decodeSingle<ProductCategory>()

            Result.success(result)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al crear categoría: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateCategory(categoryId: Int, updates: CategoryRequest): Result<Unit> {
        return try {
            supabase.from("product_categories")
                .update(updates) {
                    filter { eq("id", categoryId) }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al actualizar categoría: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deleteCategory(categoryId: Int): Result<Unit> {
        return try {
            supabase.from("product_categories")
                .delete {
                    filter { eq("id", categoryId) }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al eliminar categoría: ${e.message}")
            Result.failure(e)
        }
    }

    // ===== MÉTODOS PARA PRODUCTOS POR CATEGORÍA =====

    suspend fun getProductsByCategory(businessId: Int): Result<List<ProductsByCategory>> {
        return try {
            // Opción 1: Usando la función RPC de PostgreSQL
            val result = supabase.postgrest.rpc(
                function = "get_products_by_category",
                parameters = mapOf("business_id_param" to businessId)
            ).decodeList<ProductsByCategory>()

            Result.success(result)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al obtener productos por categoría (RPC): ${e.message}")

            // Opción 2: Fallback - Agrupar manualmente
            try {
                val products = getProductsByBusinessId(businessId)
                val grouped = products.groupBy { it.categoryId ?: 0 }

                val result = grouped.map { (categoryId, products) ->
                    val categoryName = if (categoryId == 0) {
                        "Sin categoría"
                    } else {
                        // Buscar nombre de categoría
                        try {
                            supabase.from("product_categories")
                                .select {
                                    filter { eq("id", categoryId) }
                                }
                                .decodeSingle<ProductCategory>().name
                        } catch (e: Exception) {
                            "Categoría $categoryId"
                        }
                    }

                    ProductsByCategory(
                        categoryId = categoryId,
                        categoryName = categoryName,
                        productCount = products.size,
                        products = products
                    )
                }

                Result.success(result)
            } catch (fallbackError: Exception) {
                Log.e("BusinessRepository", "Error en fallback: ${fallbackError.message}")
                Result.failure(fallbackError)
            }
        }
    }

    // ===== MÉTODOS PARA STOCK BAJO =====

    suspend fun getLowStockProducts(businessId: Int): Result<List<LowStockProduct>> {
        return try {
            // Opción 1: Usando la vista low_stock_products
            val result = supabase.from("low_stock_products")
                .select {
                    filter { eq("business_id", businessId) }
                }
                .decodeList<LowStockProduct>()

            Result.success(result)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al obtener productos con stock bajo (vista): ${e.message}")

            // Opción 2: Fallback - Filtrar manualmente
            try {
                val allProducts = getProductsByBusinessId(businessId)
                val business = getBusinessById(businessId.toString())

                val lowStock = allProducts
                    .filter { it.stockAlertEnabled && it.stock <= it.minStock }
                    .map { product ->
                        LowStockProduct(
                            id = product.id ?: 0,
                            businessId = businessId,
                            name = product.name,
                            currentStock = product.stock,
                            minStock = product.minStock,
                            price = product.price,
                            imageUrl = product.imageUrl,
                            businessName = business?.name ?: "",
                            ownerId = business?.ownerId.toString()
                        )
                    }

                Result.success(lowStock)
            } catch (fallbackError: Exception) {
                Log.e("BusinessRepository", "Error en fallback: ${fallbackError.message}")
                Result.failure(fallbackError)
            }
        }
    }

    // ===== MÉTODOS PARA MOVIMIENTOS DE STOCK =====
    suspend fun getStockMovements(productId: Int): Result<List<StockMovement>> {
        return try {
            val movements = supabase.from("stock_movements")
                .select {
                    filter { eq("product_id", productId) }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<StockMovement>()

            Result.success(movements)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al obtener movimientos de stock: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun adjustStock(adjustment: StockAdjustmentRequest): Result<Unit> {
        return try {
            // Obtener el producto actual para validar
            val product = supabase.from("products")
                .select {
                    filter { eq("id", adjustment.productId) }
                }
                .decodeSingle<ProductPayload>()

            val currentStock = product.stock
            val newStock = when (adjustment.movementType) {
                StockMovementType.RESTOCK, StockMovementType.RETURN ->
                    currentStock + adjustment.quantity
                StockMovementType.ADJUSTMENT ->
                    adjustment.quantity // En ajuste, quantity es el nuevo stock total
                StockMovementType.SALE ->
                    currentStock - adjustment.quantity
            }

            if (newStock < 0) {
                return Result.failure(Exception("Stock no puede ser negativo"))
            }

            // Crear movimiento de stock
            val movement = mapOf(
                "product_id" to adjustment.productId,
                "movement_type" to adjustment.movementType.name.lowercase(),
                "quantity" to adjustment.quantity,
                "previous_stock" to currentStock,
                "new_stock" to newStock,
                "notes" to adjustment.notes,
                "created_by" to supabase.auth.currentUserOrNull()?.id
            )

            supabase.from("stock_movements").insert(movement)

            // Actualizar stock del producto
            supabase.from("products")
                .update(
                    mapOf(
                        "stock" to newStock,
                        "updated_at" to "now()"
                    )
                ) {
                    filter { eq("id", adjustment.productId) }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al ajustar stock: ${e.message}")
            Result.failure(e)
        }
    }

    // ===== METODO AUXILIAR PARA VERIFICAR STOCK BAJO =====

    suspend fun checkLowStockAlerts(businessId: Int): Result<Int> {
        return try {
            val lowStock = getLowStockProducts(businessId)
            if (lowStock.isSuccess) {
                Result.success(lowStock.getOrNull()?.size ?: 0)
            } else {
                Result.failure(lowStock.exceptionOrNull() ?: Exception("Error desconocido"))
            }
        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error al verificar alertas de stock: ${e.message}")
            Result.failure(e)
        }
    }
}