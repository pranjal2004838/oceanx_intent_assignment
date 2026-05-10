package com.oceanx.grocery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.grocery.R
import com.oceanx.grocery.data.models.CartItem
import com.oceanx.grocery.databinding.ItemCartBinding

interface CartAdapterListener {
    fun onRemove(item: CartItem)
    fun onQuantityChanged(item: CartItem, newQuantity: Int)
}

class CartAdapter(
    private val listener: CartAdapterListener
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.apply {
                cartItemName.text = item.product.name
                cartItemUnit.text = item.product.unit
                cartItemPrice.text = "₹${item.product.price.toInt()}"
                cartItemTotal.text = "₹${item.totalPrice.toInt()}"
                quantityValue.text = item.quantity.toString()

                // Quantity controls
                decrementBtn.setOnClickListener {
                    if (item.quantity > 1) {
                        listener.onQuantityChanged(item, item.quantity - 1)
                    } else {
                        listener.onRemove(item)
                    }
                }

                incrementBtn.setOnClickListener {
                    listener.onQuantityChanged(item, item.quantity + 1)
                }

                removeBtn.setOnClickListener {
                    listener.onRemove(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}
