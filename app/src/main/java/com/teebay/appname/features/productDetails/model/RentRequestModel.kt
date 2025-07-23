package com.teebay.appname.features.productDetails.model

data class RentRequestModel(
    val product: Int?,
    val rent_option: String?,
    val rent_period_end_date: String?,
    val rent_period_start_date: String?,
    val renter: Int?
)