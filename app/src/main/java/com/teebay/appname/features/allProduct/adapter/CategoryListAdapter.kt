package com.teebay.appname.features.allProduct.adapter

import androidx.recyclerview.widget.RecyclerView
import com.teebay.appname.features.myProduct.model.Category
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.teebay.appname.R
import com.teebay.appname.databinding.CategoryItemViewBinding

class CategoryListAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.apply {
            tvCategory.text = category.label
            cardView.setCardBackgroundColor(
                if (category.isSelected) {
                    ContextCompat.getColor(holder.binding.root.context, R.color.selectedColor)
                } else {
                    ContextCompat.getColor(holder.binding.root.context, R.color.white)
                }
            )
        }

        holder.itemView.setOnClickListener {
            category.isSelected = category.isSelected.xor(true)
            notifyItemChanged(position)
            onItemClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    inner class ViewHolder(val binding: CategoryItemViewBinding) : RecyclerView.ViewHolder(binding.root)
}