package com.oceanx.grocery.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oceanx.grocery.data.models.Product
import com.oceanx.grocery.data.repository.GroceryRepository

class HomeViewModel : ViewModel() {

    private val repository = GroceryRepository.getInstance()

    val products: LiveData<List<Product>> = repository.allProducts
    val categories = repository.categories
    val isLoading: LiveData<Boolean> = repository.isLoading
    val errorMessage: LiveData<String?> = repository.errorMessage

    private val _selectedCategory = MutableLiveData<String?>(null)
    val selectedCategory: LiveData<String?> = _selectedCategory

    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> = _filteredProducts

    init {
        // Show all products initially
        _filteredProducts.value = repository.allProducts.value ?: emptyList()
    }

    fun selectCategory(categoryName: String?) {
        _selectedCategory.value = categoryName

        _filteredProducts.value = if (categoryName == null) {
            repository.allProducts.value ?: emptyList()
        } else {
            repository.getProductsByCategory(categoryName)
        }
    }

    fun addToCart(product: Product) {
        if (product.name.isBlank() || product.price <= 0) {
            // Silently fail on invalid product
            return
        }
        repository.addToCart(product)
    }

    fun searchProducts(query: String) {
        val results = repository.searchProducts(query).value ?: emptyList()
        _filteredProducts.value = results
        _selectedCategory.value = null // Clear category filter when searching
    }

    fun clearSearch() {
        _filteredProducts.value = repository.allProducts.value ?: emptyList()
        _selectedCategory.value = null
    }
}
