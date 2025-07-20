package com.teebay.appname.features.myProduct.repository

import com.teebay.appname.features.myProduct.model.AddProductRequestModel
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import com.teebay.appname.features.myProduct.service.ProductApiService
import com.teebay.appname.network.mapResult
import com.teebay.appname.network.safeApiCall
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ProductApiService
) {
    suspend fun postProduct(request: AddProductRequestModel): Result<AddProductResponseModel> =
        safeApiCall { apiService.postProduct(request).mapResult() }
}