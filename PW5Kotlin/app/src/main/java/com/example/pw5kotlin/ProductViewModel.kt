package com.example.pw5kotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pw5kotlin.data.ProductEntity
import com.example.pw5kotlin.data.ProductRepository
import com.example.pw5kotlin.network.RetrofitInstance
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val allProducts: LiveData<List<ProductEntity>>

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao, RetrofitInstance.api)
        allProducts = repository.allProducts
    }
    fun fetchAllProducts() = viewModelScope.launch {
        repository.fetchAllProducts()
    }
}
