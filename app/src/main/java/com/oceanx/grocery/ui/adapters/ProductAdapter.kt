package com.oceanx.grocery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oceanx.grocery.R
import com.oceanx.grocery.data.models.Product
import com.oceanx.grocery.databinding.ItemProductBinding

interface ProductAdapterListener {
    fun onAddToCart(product: Product)
    fun onProductClick(product: Product)
    fun onIncreaseQuantity(product: Product)
    fun onDecreaseQuantity(product: Product)
}

class ProductAdapter(
    private val products: List<Product>,
    private val listener: ProductAdapterListener,
    private val cartQuantities: Map<String, Int> = emptyMap()
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = "₹${product.price.toInt()}"
                productUnit.text = product.unit
                productRating.text = "⭐ ${product.rating}"
                val quantity = cartQuantities[product.id] ?: 0

                if (product.imageUrl.isNotEmpty()) {
                    productImagePlaceholder.visibility = android.view.View.GONE
                    Glide.with(itemView.context)
                        .load(product.imageUrl)
                        .into(productImage)
                } else {
                    productImagePlaceholder.visibility = android.view.View.VISIBLE
                    productImage.setImageDrawable(null)
                }

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

                if (product.inStock) {
                    if (quantity > 0) {
                        addToCartBtn.text = quantity.toString()
                        addToCartBtn.setOnClickListener {
                            listener.onIncreaseQuantity(product)
                        }
                        addToCartBtn.setOnLongClickListener {
                            listener.onDecreaseQuantity(product)
                            true
                        }
                    } else {
                        addToCartBtn.text = itemView.context.getString(R.string.add_to_cart)
                        addToCartBtn.setOnClickListener {
                            listener.onAddToCart(product)
                        }
                        addToCartBtn.setOnLongClickListener(null)
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
