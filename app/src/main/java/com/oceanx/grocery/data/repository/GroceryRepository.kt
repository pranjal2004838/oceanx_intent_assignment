package com.oceanx.grocery.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oceanx.grocery.data.models.CartItem
import com.oceanx.grocery.data.models.Category
import com.oceanx.grocery.data.models.Order
import com.oceanx.grocery.data.models.OrderStatus
import com.oceanx.grocery.data.models.Product
import kotlinx.coroutines.delay

/**
 * Single source of truth for app data.
 * Handles mock data generation and all CRUD operations.
 * In production, this would call actual APIs.
 */
class GroceryRepository {

    private val _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>> = _allProducts

    private val _cart = MutableLiveData<List<CartItem>>(emptyList())
    val cart: LiveData<List<CartItem>> = _cart

    private val _orders = MutableLiveData<List<Order>>(emptyList())
    val orders: LiveData<List<Order>> = _orders

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _currentOrder = MutableLiveData<Order?>(null)
    val currentOrder: LiveData<Order?> = _currentOrder

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadInitialData()
    }

    // Load mock data on initialization
    private fun loadInitialData() {
        _categories.value = generateMockCategories()
        _allProducts.value = generateMockProducts()
        _orders.value = generateMockOrders()
    }

    /**
     * Generate mock categories
     */
    private fun generateMockCategories(): List<Category> {
        return listOf(
            Category("1", "Vegetables", "🥬"),
            Category("2", "Fruits", "🍎"),
            Category("3", "Dairy", "🥛"),
            Category("4", "Snacks", "🍪"),
            Category("5", "Beverages", "☕"),
            Category("6", "Staples", "🌾")
        )
    }

    /**
     * Generate 20+ mock products covering all categories
     * Real data would come from API
     */
    private fun generateMockProducts(): List<Product> {
        return listOf(
            // Vegetables
            Product(
                id = "p1",
                imageUrl = "https://images.unsplash.com/photo-1546094096-0df4bcaaa337?q=80&w=400&auto=format&fit=crop",
                name = "Fresh Tomatoes",
                category = "Vegetables",
                price = 40.0,
                originalPrice = 50.0,
                description = "Ripe tomatoes from local farm",
                unit = "(500g)",
                rating = 4.8f
            ),
            Product(
                id = "p2",
                imageUrl = "https://images.unsplash.com/photo-1587049352847-4d4b12405451?q=80&w=400&auto=format&fit=crop",
                name = "Onions",
                category = "Vegetables",
                price = 25.0,
                description = "Fresh red onions",
                unit = "(1kg)",
                rating = 4.5f
            ),
            Product(
                id = "p3",
                imageUrl = "https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?q=80&w=400&auto=format&fit=crop",
                name = "Carrots",
                category = "Vegetables",
                price = 35.0,
                originalPrice = 45.0,
                description = "Organic carrots",
                unit = "(500g)",
                rating = 4.6f
            ),
            Product(
                id = "p4",
                imageUrl = "https://images.unsplash.com/photo-1518977676601-b53f82aba655?q=80&w=400&auto=format&fit=crop",
                name = "Potatoes",
                category = "Vegetables",
                price = 30.0,
                description = "Starchy potatoes perfect for cooking",
                unit = "(1kg)",
                rating = 4.4f
            ),
            Product(
                id = "p5",
                name = "Broccoli",
                category = "Vegetables",
                price = 60.0,
                originalPrice = 80.0,
                description = "Fresh green broccoli",
                unit = "(500g)",
                rating = 4.7f
            ),

            // Fruits
            Product(
                id = "p6",
                imageUrl = "https://images.unsplash.com/photo-1560806887-1e4cd0b6fac6?q=80&w=400&auto=format&fit=crop",
                name = "Apples",
                category = "Fruits",
                price = 80.0,
                description = "Fresh red apples",
                unit = "(1kg)",
                rating = 4.9f
            ),
            Product(
                id = "p7",
                imageUrl = "https://images.unsplash.com/photo-1481349518771-20055b2a7b24?q=80&w=400&auto=format&fit=crop",
                name = "Bananas",
                category = "Fruits",
                price = 50.0,
                originalPrice = 60.0,
                description = "Ripe yellow bananas",
                unit = "(1kg)",
                rating = 4.8f
            ),
            Product(
                id = "p8",
                name = "Oranges",
                category = "Fruits",
                price = 75.0,
                description = "Sweet organic oranges",
                unit = "(1kg)",
                rating = 4.6f
            ),
            Product(
                id = "p9",
                name = "Grapes",
                category = "Fruits",
                price = 120.0,
                description = "Fresh green grapes",
                unit = "(500g)",
                rating = 4.7f
            ),

            // Dairy
            Product(
                id = "p10",
                imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?q=80&w=400&auto=format&fit=crop",
                name = "Whole Milk",
                category = "Dairy",
                price = 60.0,
                description = "Fresh pasteurized milk",
                unit = "(1L)",
                rating = 4.5f
            ),
            Product(
                id = "p11",
                name = "Yogurt",
                category = "Dairy",
                price = 45.0,
                description = "Plain Greek yogurt",
                unit = "(500g)",
                rating = 4.6f
            ),
            Product(
                id = "p12",
                name = "Cheese",
                category = "Dairy",
                price = 150.0,
                originalPrice = 200.0,
                description = "Cheddar cheese block",
                unit = "(250g)",
                rating = 4.8f
            ),
            Product(
                id = "p13",
                name = "Butter",
                category = "Dairy",
                price = 200.0,
                description = "Salted butter",
                unit = "(250g)",
                rating = 4.7f
            ),

            // Snacks
            Product(
                id = "p14",
                imageUrl = "https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?q=80&w=400&auto=format&fit=crop",
                name = "Chips",
                category = "Snacks",
                price = 30.0,
                description = "Crispy potato chips",
                unit = "(100g)",
                rating = 4.4f
            ),
            Product(
                id = "p15",
                name = "Cookies",
                category = "Snacks",
                price = 50.0,
                originalPrice = 70.0,
                description = "Chocolate chip cookies",
                unit = "(200g)",
                rating = 4.6f
            ),
            Product(
                id = "p16",
                name = "Nuts Mix",
                category = "Snacks",
                price = 250.0,
                description = "Mixed dry fruits and nuts",
                unit = "(500g)",
                rating = 4.9f
            ),
            Product(
                id = "p17",
                name = "Popcorn",
                category = "Snacks",
                price = 40.0,
                description = "Buttered popcorn",
                unit = "(150g)",
                rating = 4.5f
            ),

            // Beverages
            Product(
                id = "p18",
                imageUrl = "https://images.unsplash.com/photo-1600271886742-f049cd451b02?q=80&w=400&auto=format&fit=crop",
                name = "Orange Juice",
                category = "Beverages",
                price = 80.0,
                description = "Fresh squeezed orange juice",
                unit = "(1L)",
                rating = 4.7f
            ),
            Product(
                id = "p19",
                name = "Coffee",
                category = "Beverages",
                price = 300.0,
                originalPrice = 400.0,
                description = "Premium arabica coffee beans",
                unit = "(500g)",
                rating = 4.8f
            ),
            Product(
                id = "p20",
                name = "Tea",
                category = "Beverages",
                price = 150.0,
                description = "Assorted tea bags",
                unit = "(50 bags)",
                rating = 4.6f
            ),

            // Staples
            Product(
                id = "p21",
                name = "Rice",
                category = "Staples",
                price = 60.0,
                description = "Basmati rice",
                unit = "(1kg)",
                rating = 4.8f
            ),
            Product(
                id = "p22",
                name = "Flour",
                category = "Staples",
                price = 40.0,
                description = "Wheat flour",
                unit = "(1kg)",
                rating = 4.5f
            ),
            Product(
                id = "p23",
                name = "Oil",
                category = "Staples",
                price = 180.0,
                originalPrice = 220.0,
                description = "Pure mustard oil",
                unit = "(1L)",
                rating = 4.7f
            )
        )
    }

    /**
     * Generate mock past orders
     */
    private fun generateMockOrders(): List<Order> {
        val baseTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000) // Yesterday
        return listOf(
            Order(
                orderId = "ord1",
                items = listOf(
                    CartItem(generateMockProducts()[0], 2),
                    CartItem(generateMockProducts()[1], 1)
                ),
                totalAmount = 105.0,
                status = OrderStatus.DELIVERED,
                createdAt = baseTime,
                deliveryAddress = "123 Main St, City"
            ),
            Order(
                orderId = "ord2",
                items = listOf(CartItem(generateMockProducts()[5], 3)),
                totalAmount = 240.0,
                status = OrderStatus.DELIVERED,
                createdAt = baseTime - (2 * 60 * 60 * 1000),
                deliveryAddress = "456 Oak Ave, City"
            )
        )
    }

    // ==================== Cart Operations ====================

    fun addToCart(product: Product) {
        val currentCart = _cart.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            // Product already in cart, increment quantity
            currentCart[currentCart.indexOf(existingItem)] =
                existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // New product, add to cart
            currentCart.add(CartItem(product, 1))
        }

        _cart.value = currentCart
    }

    fun removeFromCart(productId: String) {
        val currentCart = _cart.value?.toMutableList() ?: return
        currentCart.removeAll { it.product.id == productId }
        _cart.value = currentCart
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }

        val currentCart = _cart.value?.toMutableList() ?: return
        val itemIndex = currentCart.indexOfFirst { it.product.id == productId }

        if (itemIndex >= 0) {
            val item = currentCart[itemIndex]
            currentCart[itemIndex] = item.copy(quantity = quantity)
            _cart.value = currentCart
        }
    }

    fun getCartTotal(): Double {
        return _cart.value?.sumOf { it.totalPrice } ?: 0.0
    }

    fun getCartItemCount(): Int {
        return _cart.value?.sumOf { it.quantity } ?: 0
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // ==================== Order Operations ====================

    /**
     * Simulation of checkout - creates order and clears cart
     * Includes error state handling for edge cases
     */
    suspend fun checkoutOrder(deliveryAddress: String, promoCode: String = ""): Boolean {
        return try {
            _isLoading.value = true
            _errorMessage.value = null

            // Validate inputs
            if (deliveryAddress.isBlank()) {
                _errorMessage.value = "Delivery address cannot be empty"
                return false
            }

            val cartItems = _cart.value ?: emptyList()
            if (cartItems.isEmpty()) {
                _errorMessage.value = "Cart is empty"
                return false
            }

            // Simulate network delay
            delay(1500)

            val cartTotal = getCartTotal()
            val discountPercent = applyPromoCode(promoCode)
            val totalAmount = (cartTotal - (cartTotal * discountPercent)).coerceAtLeast(0.0)
            val newOrder = Order(
                orderId = "ord${System.currentTimeMillis()}",
                items = cartItems,
                totalAmount = totalAmount,
                status = OrderStatus.CONFIRMED,
                deliveryAddress = deliveryAddress,
                estimatedDelivery = 15 // 15 minutes estimated
            )

            // Add to orders list
            val currentOrders = _orders.value?.toMutableList() ?: mutableListOf()
            currentOrders.add(0, newOrder)
            _orders.value = currentOrders

            _currentOrder.value = newOrder
            clearCart()

            true
        } catch (e: Exception) {
            _errorMessage.value = "Error processing order: ${e.message}"
            false
        } finally {
            _isLoading.value = false
        }
    }

    fun getOrderById(orderId: String): Order? {
        return _orders.value?.find { it.orderId == orderId }
    }

    // ==================== Search Operations ====================

    fun searchProducts(query: String): LiveData<List<Product>> {
        val result = MutableLiveData<List<Product>>()
        val allProducts = _allProducts.value ?: emptyList()

        val filtered = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
            }
        }

        result.value = filtered
        return result
    }

    fun getProductsByCategory(category: String): List<Product> {
        return _allProducts.value?.filter { it.category == category } ?: emptyList()
    }

    // ==================== Discount Simulation ====================

    fun applyPromoCode(code: String): Double {
        return when (code.uppercase()) {
            "SAVE10" -> 0.1  // 10% discount
            "SAVE20" -> 0.2  // 20% discount
            else -> 0.0
        }
    }

    companion object {
        @Volatile
        private var instance: GroceryRepository? = null

        fun getInstance(): GroceryRepository {
            return instance ?: synchronized(this) {
                GroceryRepository().also { instance = it }
            }
        }
    }
}
