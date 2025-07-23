package com.teebay.appname.features.productDetails.model

import com.google.gson.annotations.SerializedName

data class RentRequestModel(
    val product: Int?,
    @SerializedName("rent_option")
    val rentOption: String?,
    @SerializedName("rent_period_start_date")
    val startDate: String?,
    @SerializedName("rent_period_end_date")
    val endDate: String?,
    val renter: Int?
)