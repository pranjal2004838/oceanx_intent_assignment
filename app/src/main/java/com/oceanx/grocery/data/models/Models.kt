package com.oceanx.grocery.data.models

import java.util.UUID

// Product model
data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val imageUrl: String = "",
    val description: String = "",
    val unit: String = "(500g)", // e.g., (1kg), (500g), (1L)
    val rating: Float = 4.5f,
    val inStock: Boolean = true
) {
    val discount: Int?
        get() = if (originalPrice != null && originalPrice > 0) {
            ((originalPrice - price) / originalPrice * 100).toInt()
        } else null
}

// Cart item (product + quantity)
data class CartItem(
    val product: Product = Product(),
    val quantity: Int = 1
) {
    val totalPrice: Double get() = product.price * quantity
}

// Order model
data class Order(
    val orderId: String = UUID.randomUUID().toString(),
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val estimatedDelivery: Int = 10, // minutes
    val deliveryAddress: String = "123 Main Street, City"
)

enum class OrderStatus {
    PENDING, CONFIRMED, PREPARING, ON_THE_WAY, DELIVERED, CANCELLED
}

// Category for grouping products
data class Category(
    val id: String = "",
    val name: String = "",
    val icon: String = ""
)
