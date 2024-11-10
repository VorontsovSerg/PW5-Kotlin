package com.example.pw5kotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pw5kotlin.network.models.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String = "Unknown",
    val category: String = "Uncategorized",
    val thumbnail: String = "",
    val discountAmount: Double = 0.0
) {
    companion object {
        fun fromProduct(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                title = product.title ?: "No Title",
                description = product.description ?: "No Description",
                price = product.price ?: 0.0,
                discountPercentage = product.discountPercentage ?: 0.0,
                rating = product.rating ?: 0.0,
                stock = product.stock ?: 0,
                brand = product.brand ?: "Unknown",
                category = product.category ?: "Uncategorized",
                thumbnail = product.thumbnail ?: "",
                discountAmount = 0.0
            )
        }
    }
}
