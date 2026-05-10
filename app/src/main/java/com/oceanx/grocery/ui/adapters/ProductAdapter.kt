package com.oceanx.grocery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.grocery.R
import com.oceanx.grocery.data.models.Product
import com.oceanx.grocery.databinding.ItemProductBinding

interface ProductAdapterListener {
    fun onAddToCart(product: Product)
    fun onProductClick(product: Product)
}

class ProductAdapter(
    private val products: List<Product>,
    private val listener: ProductAdapterListener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = "₹${product.price.toInt()}"
                productUnit.text = product.unit
                productRating.text = "⭐ ${product.rating}"

                // Show discount if available
                if (product.discount != null) {
                    productDiscount.text = "${product.discount}% OFF"
                    productDiscount.visibility = android.view.View.VISIBLE
                    productOriginalPrice.text = "₹${product.originalPrice?.toInt()}"
                    productOriginalPrice.visibility = android.view.View.VISIBLE
                } else {
                    productDiscount.visibility = android.view.View.GONE
                    productOriginalPrice.visibility = android.view.View.GONE
                }

                // Stock indicator
                if (!product.inStock) {
                    addToCartBtn.isEnabled = false
                    addToCartBtn.text = "Out of Stock"
                } else {
                    addToCartBtn.isEnabled = true
                    addToCartBtn.text = itemView.context.getString(R.string.add_to_cart)
                }

                // Click listeners
                addToCartBtn.setOnClickListener {
                    if (product.inStock) {
                        listener.onAddToCart(product)
                    }
                }

                root.setOnClickListener {
                    listener.onProductClick(product)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size
}
