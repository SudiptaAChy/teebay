package com.teebay.appname.features.myOrders.model

import com.google.gson.annotations.SerializedName

data class PurchaseResponseModel(
    val buyer: Int?,
    val id: Int?,
    val product: Int?,
    @SerializedName("purchase_date")
    val purchaseDate: String?,
    val seller: Int?
)