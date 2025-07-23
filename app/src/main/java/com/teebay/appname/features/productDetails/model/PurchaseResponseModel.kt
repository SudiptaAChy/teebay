package com.teebay.appname.features.productDetails.model

data class PurchaseResponseModel(
    val buyer: Int?,
    val id: Int?,
    val product: Int?,
    val purchase_date: String?,
    val seller: Int?
)