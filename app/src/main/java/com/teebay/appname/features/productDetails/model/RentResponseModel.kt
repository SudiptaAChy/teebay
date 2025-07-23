package com.teebay.appname.features.productDetails.model

import com.google.gson.annotations.SerializedName

data class RentResponseModel(
    val id: Int?,
    val product: Int?,
    @SerializedName("rent_date")
    val rentDate: String?,
    @SerializedName("rent_option")
    val rentOption: String?,
    @SerializedName("rent_period_start_date")
    val startDate: String?,
    @SerializedName("rent_period_end_date")
    val endDate: String?,
    val renter: Int?,
    val seller: Int?,
    @SerializedName("total_price")
    val totalPrice: String?
)