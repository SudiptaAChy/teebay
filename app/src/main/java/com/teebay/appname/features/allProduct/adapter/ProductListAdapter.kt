package com.teebay.appname.features.allProduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teebay.appname.databinding.ProductItemViewBinding
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.utils.toMoneySign

class ProductListAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.binding.apply {
            tvTitle.text = product.title
            tvCategory.text = product.categories?.joinToString(", ")
            tvPurchasePrice.text = product.purchasePrice.toMoneySign()
            tvRentPrice.text = product.rentPrice.toMoneySign()
            tvRentOption.text = "(${product.rentOption})"
            tvDescription.text = product.description
            tvDate.text = product.datePosted
        }
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(val binding: ProductItemViewBinding) : RecyclerView.ViewHolder(binding.root)
}