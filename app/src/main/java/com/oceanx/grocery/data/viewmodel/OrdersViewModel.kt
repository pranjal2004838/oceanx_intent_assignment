package com.oceanx.grocery.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.oceanx.grocery.data.models.Order
import com.oceanx.grocery.data.models.OrderStatus
import com.oceanx.grocery.data.repository.GroceryRepository

class OrdersViewModel : ViewModel() {

    private val repository = GroceryRepository.getInstance()

    val orders: LiveData<List<Order>> = repository.orders
    val activeOrder: LiveData<Order?> = repository.orders.map { orderList ->
        orderList.find { order ->
            order.status in listOf(
                OrderStatus.PENDING,
                OrderStatus.CONFIRMED,
                OrderStatus.PREPARING,
                OrderStatus.ON_THE_WAY
            )
        }
    }

    val pastOrders: LiveData<List<Order>> = repository.orders.map { orderList ->
        orderList.filter { order ->
            order.status in listOf(OrderStatus.DELIVERED, OrderStatus.CANCELLED)
        }
    }

    fun getOrderStatusDisplay(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "Pending"
            OrderStatus.CONFIRMED -> "Confirmed"
            OrderStatus.PREPARING -> "Preparing"
            OrderStatus.PACKED -> "Packed"
            OrderStatus.ON_THE_WAY -> "On the Way"
            OrderStatus.DELIVERED -> "Delivered"
            OrderStatus.CANCELLED -> "Cancelled"
        }
    }

    fun getOrderStatusIcon(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "⏳"
            OrderStatus.CONFIRMED -> "✅"
            OrderStatus.PREPARING -> "👨‍🍳"
            OrderStatus.PACKED -> "📦"
            OrderStatus.ON_THE_WAY -> "🚗"
            OrderStatus.DELIVERED -> "✔️"
            OrderStatus.CANCELLED -> "❌"
        }
    }

    fun hasActiveOrder(): Boolean {
        return activeOrder.value != null
    }

    fun getOrderById(orderId: String): Order? {
        return repository.getOrderById(orderId)
    }
}
