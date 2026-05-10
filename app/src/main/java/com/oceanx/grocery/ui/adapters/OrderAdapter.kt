package com.oceanx.grocery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.grocery.data.models.Order
import com.oceanx.grocery.databinding.ItemOrderBinding

class OrderAdapter(
    private val orders: List<Order>,
    private val getStatusDisplay: (order: Order) -> String,
    private val getStatusIcon: (order: Order) -> String
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.apply {
                orderId.text = "Order #${order.orderId.take(8)}"
                orderAmount.text = "₹${order.totalAmount.toInt()}"
                orderStatus.text = getStatusDisplay(order)
                statusIcon.text = getStatusIcon(order)

                val itemCount = order.items.sumOf { it.quantity }
                orderItems.text = "$itemCount items"

                val dateText = android.text.format.DateFormat.format(
                    "dd MMM, hh:mm",
                    order.createdAt
                ).toString()
                orderDate.text = dateText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount() = orders.size
}
