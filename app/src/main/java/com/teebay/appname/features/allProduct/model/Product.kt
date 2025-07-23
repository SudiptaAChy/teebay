package com.teebay.appname.features.allProduct.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val categories: List<String>? = null,
    @SerializedName("date_posted")
    val datePosted: String? = null,
    val description: String? = null,
    val id: Int? = null,
    @SerializedName("product_image")
    val productImage: String? = null,
    @SerializedName("purchase_price")
    val purchasePrice: String? = null,
    @SerializedName("rent_option")
    val rentOption: String? = null,
    @SerializedName("rent_price")
    val rentPrice: String? = null,
    val seller: Int? = null,
    val title: String? = null
)