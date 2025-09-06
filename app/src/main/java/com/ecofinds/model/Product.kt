package com.ecofinds.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["sellerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val imageUrl: String = "", // Placeholder for image
    val sellerId: Long,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
