package com.oceanx.grocery.data.repository

import androidx.lifecycle.LiveData
import com.oceanx.grocery.data.models.CartItem
import com.oceanx.grocery.data.models.Category
import com.oceanx.grocery.data.models.Order
import com.oceanx.grocery.data.models.Product

/**
 * Backward compatibility wrapper for existing GroceryRepository
 * Delegates to BlinkitRepository with Blinkit features
 */
class GroceryRepositoryWrapper {

    private val blinkitRepository = BlinkitRepository.getInstance()

    val allProducts: LiveData<List<Product>> = blinkitRepository.allProducts
    val cart: LiveData<List<CartItem>> = blinkitRepository.cart
    val orders: LiveData<List<Order>> = blinkitRepository.orders
    val categories: LiveData<List<Category>> = blinkitRepository.categories
    val currentOrder: LiveData<Order?> = blinkitRepository.currentOrder
    val isLoading: LiveData<Boolean> = blinkitRepository.isLoading
    val errorMessage: LiveData<String?> = blinkitRepository.errorMessage

    fun removeFromCart(productId: String) {
        blinkitRepository.removeFromCart(productId)
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        blinkitRepository.updateQuantity(productId, quantity)
    }

    fun getCartTotal(): Double = blinkitRepository.cartTotal.value ?: 0.0

    fun applyPromoCode(code: String): Double = blinkitRepository.applyPromoCode(code)

    suspend fun checkoutOrder(address: String, promoCode: String): Boolean {
        return blinkitRepository.checkoutOrder(address, promoCode)
    }
}
