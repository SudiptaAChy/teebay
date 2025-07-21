package com.teebay.appname.features.myProduct.service

import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import com.teebay.appname.features.myProduct.model.Category
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductApiService {
    @Multipart
    @POST("products/")
    suspend fun postProduct(
        @Part("seller") seller: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("categories") categories: RequestBody,
        @Part product_image: MultipartBody.Part,
        @Part("purchase_price") purchasePrice: RequestBody,
        @Part("rent_price") rentPrice: RequestBody,
        @Part("rent_option") rentOption: RequestBody,
    ): Response<AddProductResponseModel>

    @GET("products/categories/")
    suspend fun fetchCategories(): Response<List<Category>>

    @GET("products/")
    suspend fun fetchtAllProducts(): Response<List<Product>>
}
