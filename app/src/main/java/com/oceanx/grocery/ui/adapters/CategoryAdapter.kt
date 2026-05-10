package com.oceanx.grocery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.grocery.data.models.Category
import com.oceanx.grocery.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category?) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = -1

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, position: Int) {
            binding.apply {
                categoryName.text = category.name
                categoryIcon.text = category.icon

                root.setOnClickListener {
                    val oldPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(oldPosition)
                    notifyItemChanged(position)
                    onCategoryClick(category)
                }

                // Highlight selected category
                if (position == selectedPosition) {
                    root.setBackgroundColor(root.context.getColor(android.R.color.darker_gray))
                    categoryName.setTextColor(root.context.getColor(android.R.color.white))
                } else {
                    root.setBackgroundColor(root.context.getColor(android.R.color.white))
                    categoryName.setTextColor(root.context.getColor(android.R.color.black))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position)
    }

    override fun getItemCount() = categories.size
}
