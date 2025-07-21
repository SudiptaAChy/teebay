package com.teebay.appname.features.myProduct.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class AddProductRequestModel(
    val categories: String? = null,
    @SerializedName("date_posted")
    val datePosted: String? = null,
    val description: String? = null,
    val id: Int? = null,
    @SerializedName("product_image")
    val productImage: MultipartBody.Part? = null,
    @SerializedName("purchase_price")
    val purchasePrice: String? = null,
    @SerializedName("rent_option")
    val rentOption: String? = null,
    @SerializedName("rent_price")
    val rentPrice: String? = null,
    val seller: Int? = null,
    val title: String? = null
)