package com.ecofinds.dao

import androidx.room.*
import com.ecofinds.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isAvailable = 1 ORDER BY createdAt DESC")
    suspend fun getAllAvailableProducts(): List<Product>

    @Query("SELECT * FROM products WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    suspend fun getProductsBySeller(sellerId: Long): List<Product>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Long): Product?

    @Query("SELECT * FROM products WHERE category = :category AND isAvailable = 1 ORDER BY createdAt DESC")
    suspend fun getProductsByCategory(category: String): List<Product>

    @Query("SELECT * FROM products WHERE title LIKE :searchQuery AND isAvailable = 1 ORDER BY createdAt DESC")
    suspend fun searchProducts(searchQuery: String): List<Product>

    @Insert
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}
