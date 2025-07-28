package com.teebay.appname.features.myOrders.repository

import com.teebay.appname.features.myOrders.model.PurchaseResponseModel
import com.teebay.appname.features.myOrders.model.RentResponseModel
import com.teebay.appname.features.myOrders.service.MyOrdersApiService
import com.teebay.appname.network.mapResult
import com.teebay.appname.network.safeApiCall
import javax.inject.Inject

class MyOrderRepository @Inject constructor(
    private val apiService: MyOrdersApiService
) {
    suspend fun fetchPurchasedProducts(): Result<List<PurchaseResponseModel>> =
        safeApiCall { apiService.fetchPurchasedProducts().mapResult() }

    suspend fun fetchRentedProducts(): Result<List<RentResponseModel>> =
        safeApiCall { apiService.fetchRentedProducts().mapResult() }
}