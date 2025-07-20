package com.teebay.appname.features.myProduct.service

import com.teebay.appname.features.myProduct.model.AddProductRequestModel
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProductApiService {
    @POST("products/")
    suspend fun postProduct(@Body request: AddProductRequestModel): Response<AddProductResponseModel>
}