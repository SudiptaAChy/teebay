package com.teebay.appname.features.productDetails.model

data class RentResponseModel(
    val id: Int?,
    val product: Int?,
    val rent_date: String?,
    val rent_option: String?,
    val rent_period_end_date: String?,
    val rent_period_start_date: String?,
    val renter: Int?,
    val seller: Int?,
    val total_price: String?
)