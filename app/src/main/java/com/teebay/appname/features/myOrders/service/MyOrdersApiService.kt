package com.teebay.appname.features.myOrders.service

import com.teebay.appname.features.myOrders.model.PurchaseResponseModel
import com.teebay.appname.features.myOrders.model.RentResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface MyOrdersApiService {
    @GET("transactions/purchases/")
    suspend fun fetchPurchasedProducts(): Response<List<PurchaseResponseModel>>

    @GET("transactions/rentals/")
    suspend fun fetchRentedProducts(): Response<List<RentResponseModel>>
}