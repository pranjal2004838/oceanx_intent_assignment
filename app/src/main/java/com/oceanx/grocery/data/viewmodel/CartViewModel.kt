package com.oceanx.grocery.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.oceanx.grocery.data.models.CartItem
import com.oceanx.grocery.data.repository.GroceryRepository

class CartViewModel : ViewModel() {

    private val repository = GroceryRepository.getInstance()

    val cart: LiveData<List<CartItem>> = repository.cart
    val cartTotal: LiveData<Double> = repository.cart.map { items ->
        items.sumOf { it.totalPrice }
    }

    val itemCount: LiveData<Int> = repository.cart.map { items ->
        items.sumOf { it.quantity }
    }

    fun removeItem(productId: String) {
        if (productId.isNotBlank()) {
            repository.removeFromCart(productId)
        }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        if (productId.isNotBlank() && quantity > 0) {
            repository.updateCartQuantity(productId, quantity)
        }
    }

    fun incrementQuantity(productId: String) {
        val currentCart = cart.value ?: return
        val item = currentCart.find { it.product.id == productId } ?: return
        updateQuantity(productId, item.quantity + 1)
    }

    fun decrementQuantity(productId: String) {
        val currentCart = cart.value ?: return
        val item = currentCart.find { it.product.id == productId } ?: return

        if (item.quantity > 1) {
            updateQuantity(productId, item.quantity - 1)
        } else {
            removeItem(productId)
        }
    }

    fun isCartEmpty(): Boolean {
        return cart.value.isNullOrEmpty()
    }

}
