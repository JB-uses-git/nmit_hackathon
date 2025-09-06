package com.ecofinds.dao

import androidx.room.*
import com.ecofinds.model.Purchase
import com.ecofinds.model.Product

@Dao
interface PurchaseDao {
    @Query("""
        SELECT p.* FROM products p 
        INNER JOIN purchases pur ON p.id = pur.productId 
        WHERE pur.buyerId = :buyerId 
        ORDER BY pur.purchaseDate DESC
    """)
    suspend fun getPurchaseHistoryForUser(buyerId: Long): List<Product>

    @Insert
    suspend fun insertPurchase(purchase: Purchase): Long

    @Query("SELECT * FROM purchases WHERE buyerId = :buyerId ORDER BY purchaseDate DESC")
    suspend fun getPurchasesByBuyer(buyerId: Long): List<Purchase>
}
