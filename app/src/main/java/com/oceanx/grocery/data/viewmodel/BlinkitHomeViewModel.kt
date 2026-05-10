package com.oceanx.grocery.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.oceanx.grocery.data.models.Banner
import com.oceanx.grocery.data.models.Product
import com.oceanx.grocery.data.repository.BlinkitRepository
import kotlinx.coroutines.launch

/**
 * Blinkit-style HomeViewModel with:
 * - Real-time ETA
 * - Personalized recommendations
 * - Smart search
 * - Trending items
 * - Contextual banners
 */
class BlinkitHomeViewModel : ViewModel() {

    private val repository = BlinkitRepository.getInstance()

    // All products
    val allProducts: LiveData<List<Product>> = repository.allProducts
    val trendingProducts: LiveData<List<Product>> = repository.trendingProducts

    // Personalization
    val deliveryEta: LiveData<Int> = repository.deliveryEta
    val banners: LiveData<List<Banner>> = repository.banners
    val searchSuggestions: LiveData<List<String>> = repository.searchSuggestions

    // Frequently bought together
    val frequentlyBoughtTogether: LiveData<List<Product>> = repository.recommendedProducts

    val userProfile = repository.userProfile
    val loyaltyMembership = repository.loyaltyMembership
    val cartCount: LiveData<Int> = repository.cart.map { it.size }

    // Search
    private var currentSearchResults: List<Product> = emptyList()

    /**
     * Quick add to cart - core Blinkit feature
     */
    fun quickAddToCart(product: Product) {
        repository.quickAddToCart(product)
        repository.markFrequentBuy(product.id)
    }

    /**
     * Search products
     */
    fun searchProducts(query: String) {
        repository.addToSearchHistory(query)
        repository.getSearchSuggestions(query)
        currentSearchResults = repository.searchProducts(query)
    }

    fun getSearchResults(): List<Product> = currentSearchResults

    /**
     * Get frequently bought with this product
     */
    fun getFrequentlyBoughtWith(productId: String): List<Product> {
        return repository.getFrequentlyBoughtTogether(productId)
    }

    /**
     * Simulate dynamic ETA based on location & time
     */
    fun startEtaSimulation() {
        viewModelScope.launch {
            // Dynamic ETA calculation
            var eta = 8
            while (eta > 0) {
                kotlinx.coroutines.delay(30000) // Update every 30 seconds
                eta = (Math.random() * 10 + 5).toInt()
            }
        }
    }
}
