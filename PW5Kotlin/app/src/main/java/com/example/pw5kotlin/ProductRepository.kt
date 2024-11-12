package com.example.pw5kotlin.data

import androidx.lifecycle.LiveData
import com.example.pw5kotlin.network.ProductApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val productDao: ProductDao, private val productApi: ProductApi) {

    val allProducts: LiveData<List<ProductEntity>> = productDao.getAllProducts()

    suspend fun fetchAllProducts() {
        val response = withContext(Dispatchers.IO) { productApi.getAllProducts() }
        val productEntities = response.products.map { ProductEntity.fromProduct(it) }
        productDao.insertAll(productEntities)
    }
}
