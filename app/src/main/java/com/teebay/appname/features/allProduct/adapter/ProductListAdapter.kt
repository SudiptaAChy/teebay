package com.teebay.appname.features.allProduct.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teebay.appname.R
import com.teebay.appname.databinding.ProductItemViewBinding
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.utils.formatDate
import com.teebay.appname.utils.toMoneySign
import com.teebay.appname.utils.shortenText
import okhttp3.internal.platform.PlatformRegistry.applicationContext

class ProductListAdapter(
    private var products: List<Product>,
    private val onItemClick: ((Int) -> Unit)? = null,
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    fun setData(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

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
            tvRentOption.text = applicationContext?.getString(R.string.rent_option_display, product.rentOption ?: "")
            tvDescription.text = product.description.shortenText()
            tvDate.text = formatDate(product.datePosted.toString())
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(val binding: ProductItemViewBinding) : RecyclerView.ViewHolder(binding.root)
}