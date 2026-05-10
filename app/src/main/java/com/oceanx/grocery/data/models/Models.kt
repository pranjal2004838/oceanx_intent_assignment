package com.oceanx.grocery.data.models

import java.util.UUID

// Product model - ENHANCED FOR BLINKIT
data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val imageUrl: String = "",
    val description: String = "",
    val unit: String = "(500g)",
    val rating: Float = 4.5f,
    val inStock: Boolean = true,
    val isTrending: Boolean = false,
    val isFlashSale: Boolean = false,
    val frequentlyBoughtWith: List<String> = emptyList(), // product IDs
    val substitutes: List<String> = emptyList(), // alternative products
    val nutrition: String = "", // nutritional info
    val deliveryEta: Int = 8 // minutes
) {
    val discount: Int?
        get() = if (originalPrice != null && originalPrice > 0) {
            ((originalPrice - price) / originalPrice * 100).toInt()
        } else null
}

// Cart item with timestamp (for cart editing)
data class CartItem(
    val product: Product = Product(),
    val quantity: Int = 1,
    val addedAt: Long = System.currentTimeMillis()
) {
    val totalPrice: Double get() = product.price * quantity
}

// Order model - ENHANCED WITH TRACKING
data class Order(
    val orderId: String = UUID.randomUUID().toString(),
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val estimatedDelivery: Int = 10,
    val deliveryAddress: String = "123 Main Street, City",
    val paymentMethod: String = "UPI",
    val appliedPromo: String = "",
    val discount: Double = 0.0,
    val deliveryPartner: DeliveryPartner? = null,
    val statusHistory: List<OrderStatusUpdate> = emptyList()
)

// Real-time order status updates
data class OrderStatusUpdate(
    val status: OrderStatus,
    val timestamp: Long = System.currentTimeMillis(),
    val message: String = ""
)

// Delivery Partner info
data class DeliveryPartner(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val phone: String = "",
    val rating: Float = 4.8f,
    val currentLocation: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

enum class OrderStatus {
    PENDING, CONFIRMED, PREPARING, PACKED, ON_THE_WAY, DELIVERED, CANCELLED
}

// Category for grouping products
data class Category(
    val id: String = "",
    val name: String = "",
    val icon: String = ""
)

// Personalized Banner for speed-first design
data class Banner(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val subtitle: String = "",
    val backgroundImage: String = "",
    val backgroundColor: String = "#FFD700", // Blinkit yellow
    val triggerType: BannerTrigger = BannerTrigger.TIME_OF_DAY, // Time, Weather, User Behavior
    val triggers: List<String> = emptyList() // morning, rain, etc
)

enum class BannerTrigger {
    TIME_OF_DAY, WEATHER, USER_BEHAVIOR, FESTIVAL, INVENTORY
}

// Address with location services
data class DeliveryAddress(
    val id: String = UUID.randomUUID().toString(),
    val label: String = "Home", // Home, Work, etc
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isDefault: Boolean = false
)

// User preferences and personalization
data class UserProfile(
    val userId: String = UUID.randomUUID().toString(),
    val searchHistory: List<String> = emptyList(),
    val recentProducts: List<String> = emptyList(),
    val favoriteCategories: List<String> = emptyList(),
    val isMember: Boolean = false,
    val membershipExpiryDate: Long? = null,
    val frequentBuys: Map<String, Int> = emptyMap(), // product -> buy count
    val addresses: List<DeliveryAddress> = emptyList()
)

// Promo/Loyalty info
data class PromoCode(
    val code: String = "",
    val discountPercent: Double = 0.0,
    val validUntil: Long = System.currentTimeMillis(),
    val minOrderAmount: Double = 0.0
)

// Loyalty Membership (Blinkit Gold style)
data class LoyaltyMembership(
    val tier: String = "STANDARD", // STANDARD, GOLD, PLATINUM
    val expiryDate: Long = System.currentTimeMillis(),
    val benefits: List<String> = listOf("Free delivery", "Priority support"),
    val pointsBalance: Int = 0
)
