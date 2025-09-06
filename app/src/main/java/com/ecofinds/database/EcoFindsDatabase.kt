package com.ecofinds.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.ecofinds.model.*
import com.ecofinds.dao.*

@Database(
    entities = [User::class, Product::class, CartItem::class, Purchase::class],
    version = 1,
    exportSchema = false
)
abstract class EcoFindsDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun purchaseDao(): PurchaseDao

    companion object {
        @Volatile
        private var INSTANCE: EcoFindsDatabase? = null

        fun getDatabase(context: Context): EcoFindsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EcoFindsDatabase::class.java,
                    "ecofinds_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
