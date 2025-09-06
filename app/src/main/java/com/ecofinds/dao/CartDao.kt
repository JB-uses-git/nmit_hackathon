package com.ecofinds.dao

import androidx.room.*
import com.ecofinds.model.CartItem
import com.ecofinds.model.Product

@Dao
interface CartDao {
    @Query("""
        SELECT p.* FROM products p 
        INNER JOIN cart_items c ON p.id = c.productId 
        WHERE c.userId = :userId
    """)
    suspend fun getCartItemsForUser(userId: Long): List<Product>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getCartItem(userId: Long, productId: Long): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartItem)

    @Delete
    suspend fun removeFromCart(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Long)

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    suspend fun getCartItemCount(userId: Long): Int
}
