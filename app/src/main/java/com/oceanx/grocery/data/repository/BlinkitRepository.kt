package com.oceanx.grocery.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oceanx.grocery.data.models.*
import kotlinx.coroutines.delay
import java.util.*

/**
 * Blinkit-style repository with speed-first features:
 * - Instant add-to-cart
 * - Real-time tracking
 * - Personalized recommendations
 * - Smart search
 * - Dynamic ETA
 * - Loyalty system
 */
class BlinkitRepository {

    // Products & Categories
    private val _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>> = _allProducts

    private val _trendingProducts = MutableLiveData<List<Product>>()
    val trendingProducts: LiveData<List<Product>> = _trendingProducts

    private val _recommendedProducts = MutableLiveData<List<Product>>()
    val recommendedProducts: LiveData<List<Product>> = _recommendedProducts

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    // Cart & Checkout
    private val _cart = MutableLiveData<List<CartItem>>(emptyList())
    val cart: LiveData<List<CartItem>> = _cart

    private val _cartTotal = MutableLiveData<Double>(0.0)
    val cartTotal: LiveData<Double> = _cartTotal

    private val _savingsAmount = MutableLiveData<Double>(0.0)
    val savingsAmount: LiveData<Double> = _savingsAmount

    // Orders & Tracking
    private val _orders = MutableLiveData<List<Order>>(emptyList())
    val orders: LiveData<List<Order>> = _orders

    private val _currentOrder = MutableLiveData<Order?>(null)
    val currentOrder: LiveData<Order?> = _currentOrder

    private val _orderTrackingUpdates = MutableLiveData<OrderStatusUpdate?>(null)
    val orderTrackingUpdates: LiveData<OrderStatusUpdate?> = _orderTrackingUpdates

    // Personalization & Search
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    private val _searchSuggestions = MutableLiveData<List<String>>()
    val searchSuggestions: LiveData<List<String>> = _searchSuggestions

    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    private val _deliveryEta = MutableLiveData<Int>(8)
    val deliveryEta: LiveData<Int> = _deliveryEta

    private val _userAddresses = MutableLiveData<List<DeliveryAddress>>()
    val userAddresses: LiveData<List<DeliveryAddress>> = _userAddresses

    private val _loyaltyMembership = MutableLiveData<LoyaltyMembership>()
    val loyaltyMembership: LiveData<LoyaltyMembership> = _loyaltyMembership

    // Loading & Errors
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    companion object {
        private var instance: BlinkitRepository? = null

        fun getInstance(): BlinkitRepository {
            return instance ?: synchronized(this) {
                BlinkitRepository().also { instance = it }
            }
        }
    }

    init {
        loadInitialData()
        initializeUserProfile()
        generateContextualBanners()
    }

    // ===================== DATA INITIALIZATION =====================

    private fun loadInitialData() {
        _categories.value = generateCategories()
        _allProducts.value = generateBlinkitProducts()
        _trendingProducts.value = _allProducts.value?.filter { it.isTrending } ?: emptyList()
        _orders.value = generateMockOrders()
    }

    private fun initializeUserProfile() {
        _userProfile.value = UserProfile(
            addresses = listOf(
                DeliveryAddress("addr1", "Home", "123 Main Street, City", isDefault = true),
                DeliveryAddress("addr2", "Work", "Tech Park, Building A")
            ),
            searchHistory = listOf("milk", "bread", "eggs", "coffee")
        )
        _loyaltyMembership.value = LoyaltyMembership(
            tier = "GOLD",
            benefits = listOf("Free delivery", "2-hour delivery", "Priority support", "10% cashback"),
            pointsBalance = 250
        )
        _userAddresses.value = _userProfile.value?.addresses ?: emptyList()
    }

    // ===================== CATEGORIES =====================

    private fun generateCategories(): List<Category> {
        return listOf(
            Category("1", "Vegetables", "🥬"),
            Category("2", "Fruits", "🍎"),
            Category("3", "Dairy", "🥛"),
            Category("4", "Snacks", "🍪"),
            Category("5", "Beverages", "☕"),
            Category("6", "Staples", "🌾"),
            Category("7", "Electronics", "📱"),
            Category("8", "Beauty", "💅"),
            Category("9", "Pharmacy", "💊")
        )
    }

    // ===================== PRODUCTS - BLINKIT OPTIMIZED =====================

    private fun generateBlinkitProducts(): List<Product> {
        return listOf(
            // VEGETABLES - Speed commerce favorites
            Product(
                id = "p1",
                imageUrl = "https://images.unsplash.com/photo-1546094096-0df4bcaaa337?q=80&w=400&auto=format&fit=crop",
                name = "Fresh Tomatoes",
                category = "Vegetables",
                price = 40.0,
                originalPrice = 50.0,
                description = "Ripe tomatoes",
                unit = "(500g)",
                rating = 4.8f,
                isTrending = true,
                frequentlyBoughtWith = listOf("p2", "p3", "p18"),
                deliveryEta = 8
            ),
            Product(
                id = "p2",
                imageUrl = "https://images.unsplash.com/photo-1587049352847-4d4b12405451?q=80&w=400&auto=format&fit=crop",
                name = "Onions",
                category = "Vegetables",
                price = 25.0,
                description = "Fresh red onions",
                unit = "(1kg)",
                rating = 4.5f,
                frequentlyBoughtWith = listOf("p1", "p3"),
                deliveryEta = 8
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
                rating = 4.6f,
                isTrending = true,
                deliveryEta = 8
            ),

            // DAIRY - Time-sensitive purchases
            Product(
                id = "p10",
                imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?q=80&w=400&auto=format&fit=crop",
                name = "Whole Milk",
                category = "Dairy",
                price = 60.0,
                originalPrice = 70.0,
                description = "Fresh pasteurized milk",
                unit = "(1L)",
                rating = 4.5f,
                isTrending = true,
                isFlashSale = true,
                frequentlyBoughtWith = listOf("p11", "p12"),
                deliveryEta = 8
            ),
            Product(
                id = "p11",
                name = "Yogurt",
                category = "Dairy",
                price = 45.0,
                description = "Plain Greek yogurt",
                unit = "(500g)",
                rating = 4.6f,
                frequentlyBoughtWith = listOf("p10"),
                deliveryEta = 8
            ),
            Product(
                id = "p12",
                name = "Butter",
                category = "Dairy",
                price = 200.0,
                originalPrice = 250.0,
                description = "Salted butter",
                unit = "(250g)",
                rating = 4.7f,
                frequentlyBoughtWith = listOf("p10"),
                deliveryEta = 8
            ),

            // SNACKS - Impulse purchases
            Product(
                id = "p14",
                name = "Lays Chips",
                category = "Snacks",
                price = 30.0,
                description = "Crispy potato chips",
                unit = "(100g)",
                rating = 4.4f,
                isFlashSale = true,
                frequentlyBoughtWith = listOf("p18", "p24"),
                deliveryEta = 8
            ),
            Product(
                id = "p15",
                name = "Cookies",
                category = "Snacks",
                price = 50.0,
                originalPrice = 70.0,
                description = "Chocolate chip cookies",
                unit = "(200g)",
                rating = 4.6f,
                frequentlyBoughtWith = listOf("p18"),
                deliveryEta = 8
            ),
            Product(
                id = "p16",
                name = "Nuts Mix",
                category = "Snacks",
                price = 250.0,
                description = "Mixed dry fruits and nuts",
                unit = "(500g)",
                rating = 4.9f,
                isTrending = true,
                deliveryEta = 8
            ),

            // BEVERAGES - Late-night cravings
            Product(
                id = "p18",
                name = "Coca Cola",
                category = "Beverages",
                price = 50.0,
                originalPrice = 60.0,
                description = "Cold soft drink",
                unit = "(500ml)",
                rating = 4.7f,
                isFlashSale = true,
                frequentlyBoughtWith = listOf("p14", "p15"),
                deliveryEta = 8
            ),
            Product(
                id = "p19",
                name = "Coffee",
                category = "Beverages",
                price = 300.0,
                originalPrice = 400.0,
                description = "Premium arabica coffee beans",
                unit = "(500g)",
                rating = 4.8f,
                isTrending = true,
                deliveryEta = 8
            ),

            // STAPLES
            Product(
                id = "p21",
                name = "Basmati Rice",
                category = "Staples",
                price = 60.0,
                description = "Premium basmati rice",
                unit = "(1kg)",
                rating = 4.8f,
                frequentlyBoughtWith = listOf("p23"),
                deliveryEta = 8
            ),
            Product(
                id = "p23",
                name = "Mustard Oil",
                category = "Staples",
                price = 180.0,
                originalPrice = 220.0,
                description = "Pure mustard oil",
                unit = "(1L)",
                rating = 4.7f,
                frequentlyBoughtWith = listOf("p21"),
                deliveryEta = 8
            ),

            // ELECTRONICS
            Product(
                id = "p24",
                name = "USB-C Cable",
                category = "Electronics",
                price = 150.0,
                originalPrice = 250.0,
                description = "3m USB-C charging cable",
                unit = "(1 piece)",
                rating = 4.6f,
                isFlashSale = true,
                deliveryEta = 8
            ),
            Product(
                id = "p25",
                name = "Phone Charger",
                category = "Electronics",
                price = 400.0,
                originalPrice = 600.0,
                description = "Fast 65W charger",
                unit = "(1 piece)",
                rating = 4.8f,
                isFlashSale = true,
                frequentlyBoughtWith = listOf("p24"),
                deliveryEta = 8
            ),

            // BEAUTY
            Product(
                id = "p26",
                name = "Face Wash",
                category = "Beauty",
                price = 250.0,
                originalPrice = 350.0,
                description = "Gentle face wash",
                unit = "(150ml)",
                rating = 4.7f,
                deliveryEta = 8
            ),

            // PHARMACY
            Product(
                id = "p27",
                name = "Aspirin",
                category = "Pharmacy",
                price = 50.0,
                description = "Pain reliever",
                unit = "(10 tablets)",
                rating = 4.5f,
                deliveryEta = 8
            )
        )
    }

    // ===================== QUICK COMMERCE FEATURES =====================

    /**
     * Add to cart with INSTANT feedback (key Blinkit feature)
     * No need to open product detail page
     */
    fun quickAddToCart(product: Product) {
        val currentCart = _cart.value?.toMutableList() ?: mutableListOf()
        val existing = currentCart.find { it.product.id == product.id }

        if (existing != null) {
            currentCart.remove(existing)
            currentCart.add(existing.copy(quantity = existing.quantity + 1))
        } else {
            currentCart.add(CartItem(product, 1))
        }

        _cart.value = currentCart
        updateCartTotals()
    }

    /**
     * Update quantity with immediate UI feedback
     */
    fun updateQuantity(productId: String, quantity: Int) {
        val currentCart = _cart.value?.toMutableList() ?: return
        val index = currentCart.indexOfFirst { it.product.id == productId }

        if (index != -1) {
            if (quantity > 0) {
                currentCart[index] = currentCart[index].copy(quantity = quantity)
            } else {
                currentCart.removeAt(index)
            }
            _cart.value = currentCart
            updateCartTotals()
        }
    }

    /**
     * Remove item from cart
     */
    fun removeFromCart(productId: String) {
        val currentCart = _cart.value?.toMutableList() ?: return
        currentCart.removeAll { it.product.id == productId }
        _cart.value = currentCart
        updateCartTotals()
    }

    /**
     * Calculate savings and apply loyalty benefits
     */
    private fun updateCartTotals() {
        val subtotal = _cart.value?.sumOf { it.totalPrice } ?: 0.0
        val originalTotal = _cart.value?.sumOf { it.product.originalPrice?.times(it.quantity) ?: 0.0 } ?: 0.0
        val savings = originalTotal - subtotal

        _cartTotal.value = subtotal
        _savingsAmount.value = if (savings > 0) savings else 0.0
    }

    /**
     * Search with smart suggestions (trending, recent, history)
     */
    fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return emptyList()

        val allProducts = _allProducts.value ?: return emptyList()
        return allProducts.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
    }

    /**
     * Get smart search suggestions based on history and trending
     */
    fun getSearchSuggestions(query: String) {
        val recent = _userProfile.value?.searchHistory ?: emptyList()
        val matching = searchProducts(query).map { it.name }
        val suggestions = (recent + matching).distinct().take(5)

        _searchSuggestions.value = suggestions
    }

    /**
     * Get frequently bought together for cross-selling
     */
    fun getFrequentlyBoughtTogether(productId: String): List<Product> {
        val allProducts = _allProducts.value ?: return emptyList()
        val product = allProducts.find { it.id == productId } ?: return emptyList()

        return allProducts.filter { it.id in product.frequentlyBoughtWith }
    }

    /**
     * Get substitute products for out-of-stock items
     */
    fun getSubstitutes(productId: String): List<Product> {
        val allProducts = _allProducts.value ?: return emptyList()
        val product = allProducts.find { it.id == productId } ?: return emptyList()

        return allProducts.filter { it.id in product.substitutes && it.inStock }
    }

    // ===================== PERSONALIZATION =====================

    /**
     * Generate contextual banners based on time of day
     */
    private fun generateContextualBanners() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val banners = when {
            hour in 6..9 -> listOf(
                Banner(
                    title = "Good Morning!",
                    subtitle = "Fresh milk, bread & eggs waiting",
                    backgroundColor = "#FFE5B3",
                    triggerType = BannerTrigger.TIME_OF_DAY,
                    triggers = listOf("morning")
                )
            )
            hour in 12..14 -> listOf(
                Banner(
                    title = "Lunch Time!",
                    subtitle = "Grab snacks & beverages",
                    backgroundColor = "#FFCDD2",
                    triggerType = BannerTrigger.TIME_OF_DAY,
                    triggers = listOf("noon")
                )
            )
            hour in 18..21 -> listOf(
                Banner(
                    title = "Evening Cravings?",
                    subtitle = "Ice cream, snacks & cold drinks",
                    backgroundColor = "#E1BEE7",
                    triggerType = BannerTrigger.TIME_OF_DAY,
                    triggers = listOf("evening")
                )
            )
            else -> listOf(
                Banner(
                    title = "Late Night Munchies",
                    subtitle = "24/7 delivery available",
                    backgroundColor = "#2C3E50",
                    triggerType = BannerTrigger.TIME_OF_DAY,
                    triggers = listOf("night")
                )
            )
        }

        _banners.value = banners
    }

    /**
     * Get personalized recommendations based on user history
     */
    fun getPersonalizedRecommendations(): List<Product> {
        val allProducts = _allProducts.value ?: return emptyList()
        val userProfile = _userProfile.value ?: return emptyList()

        // Recommend from favorite categories
        val fromFavorites = allProducts.filter { it.category in userProfile.favoriteCategories }
        // Add trending items
        val trending = allProducts.filter { it.isTrending }

        return (fromFavorites + trending).distinctBy { it.id }.take(10)
    }

    /**
     * Add to search history
     */
    fun addToSearchHistory(query: String) {
        val profile = _userProfile.value ?: return
        val history = (listOf(query) + profile.searchHistory).distinct().take(10)

        _userProfile.value = profile.copy(searchHistory = history)
    }

    /**
     * Mark product as frequently bought
     */
    fun markFrequentBuy(productId: String) {
        val profile = _userProfile.value ?: return
        val frequentBuys = profile.frequentBuys.toMutableMap()
        frequentBuys[productId] = (frequentBuys[productId] ?: 0) + 1

        _userProfile.value = profile.copy(frequentBuys = frequentBuys)
    }

    // ===================== CHECKOUT & ORDERS =====================

    /**
     * Apply promo code
     */
    fun applyPromoCode(code: String): Double {
        val discounts = mapOf(
            "SAVE10" to 0.10,
            "SAVE20" to 0.20,
            "WELCOME" to 0.15,
            "FLAT50" to 50.0
        )

        return discounts[code] ?: 0.0
    }

    /**
     * Checkout - create order with instant confirmation
     */
    suspend fun checkoutOrder(address: String, promoCode: String): Boolean {
        _isLoading.value = true

        return try {
            delay(1500) // Simulate API call

            val items = _cart.value ?: emptyList()
            if (items.isEmpty()) {
                _errorMessage.value = "Cart is empty"
                false
            } else {
                val subtotal = items.sumOf { it.totalPrice }
                val discountPercent = applyPromoCode(promoCode)
                val discount = if (discountPercent < 1) subtotal * discountPercent else discountPercent
                val total = subtotal - discount

                val order = Order(
                    items = items,
                    totalAmount = total,
                    deliveryAddress = address,
                    appliedPromo = if (discountPercent > 0) promoCode else "",
                    discount = discount,
                    status = OrderStatus.CONFIRMED,
                    deliveryPartner = generateMockDeliveryPartner()
                )

                _currentOrder.value = order

                // Add to orders history
                val orders = _orders.value?.toMutableList() ?: mutableListOf()
                orders.add(0, order)
                _orders.value = orders

                // Clear cart
                _cart.value = emptyList()
                updateCartTotals()

                // Start tracking simulation
                simulateOrderTracking(order)

                true
            }
        } catch (e: Exception) {
            _errorMessage.value = e.message
            false
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * Simulate real-time order tracking
     */
    private suspend fun simulateOrderTracking(order: Order) {
        val statuses = listOf(
            OrderStatus.CONFIRMED to "Order confirmed! Preparing items...",
            OrderStatus.PREPARING to "Packing your order...",
            OrderStatus.PACKED to "Order packed and ready to pick up",
            OrderStatus.ON_THE_WAY to "Rider is on the way with your order",
            OrderStatus.DELIVERED to "Order delivered! Thank you for shopping"
        )

        for ((status, message) in statuses) {
            delay(2000)

            _orderTrackingUpdates.value = OrderStatusUpdate(
                status = status,
                timestamp = System.currentTimeMillis(),
                message = message
            )

            _currentOrder.value = order.copy(
                status = status,
                statusHistory = order.statusHistory + OrderStatusUpdate(status, System.currentTimeMillis(), message)
            )
        }
    }

    // ===================== ADDRESSES & DELIVERY =====================

    /**
     * Add or update delivery address
     */
    fun addAddress(address: DeliveryAddress) {
        val addresses = _userAddresses.value?.toMutableList() ?: mutableListOf()
        addresses.add(address)
        _userAddresses.value = addresses

        _deliveryEta.value = 8 // Default ETA
    }

    /**
     * Calculate delivery ETA based on time and location
     */
    fun calculateDeliveryEta(address: DeliveryAddress): Int {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when {
            hour in 6..10 -> 10 // Morning rush
            hour in 12..14 -> 12 // Lunch rush
            hour in 18..21 -> 15 // Evening rush
            else -> 8 // Off-peak
        }
    }

    // ===================== LOYALTY & MEMBERSHIP =====================

    /**
     * Get loyalty benefits
     */
    fun getLoyaltyBenefits(): List<String> {
        return _loyaltyMembership.value?.benefits ?: emptyList()
    }

    /**
     * Add loyalty points on order
     */
    fun addLoyaltyPoints(amount: Double) {
        val membership = _loyaltyMembership.value ?: return
        val points = (amount / 100).toInt() // 1 point per ₹100

        _loyaltyMembership.value = membership.copy(
            pointsBalance = membership.pointsBalance + points
        )
    }

    // ===================== MOCK DATA =====================

    private fun generateMockOrders(): List<Order> {
        return listOf(
            Order(
                orderId = "ORD001",
                items = listOf(
                    CartItem(Product(id = "p10",
                imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?q=80&w=400&auto=format&fit=crop", name = "Milk", category = "Dairy", price = 60.0), 2)
                ),
                totalAmount = 120.0,
                status = OrderStatus.DELIVERED,
                estimatedDelivery = 8
            ),
            Order(
                orderId = "ORD002",
                items = listOf(
                    CartItem(Product(id = "p1",
                imageUrl = "https://images.unsplash.com/photo-1546094096-0df4bcaaa337?q=80&w=400&auto=format&fit=crop", name = "Tomatoes", category = "Vegetables", price = 40.0), 1)
                ),
                totalAmount = 40.0,
                status = OrderStatus.DELIVERED,
                estimatedDelivery = 8
            )
        )
    }

    private fun generateMockDeliveryPartner(): DeliveryPartner {
        return DeliveryPartner(
            name = "Raj Kumar",
            phone = "+91-98765-43210",
            rating = 4.8f,
            currentLocation = "2 km away"
        )
    }
}
