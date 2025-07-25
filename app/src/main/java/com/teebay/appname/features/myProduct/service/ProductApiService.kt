package com.teebay.appname.features.myProduct.service

import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import com.teebay.appname.features.myProduct.model.Category
import com.teebay.appname.features.productDetails.model.PurchaseRequestModel
import com.teebay.appname.features.productDetails.model.PurchaseResponseModel
import com.teebay.appname.features.productDetails.model.RentRequestModel
import com.teebay.appname.features.productDetails.model.RentResponseModel
import com.teebay.appname.network.EmptyResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

    @POST("transactions/purchases/")
    suspend fun purchase(
        @Body request: PurchaseRequestModel
    ): Response<PurchaseResponseModel>

    @POST("transactions/rentals/")
    suspend fun rent(
        @Body request: RentRequestModel
    ): Response<RentResponseModel>

    @DELETE("products/{id}/")
    suspend fun deleteProduct(
        @Path("id") id: Int
    ): Response<EmptyResponseModel>

    @Multipart
    @PATCH("products/{id}/")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Part("seller") seller: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("categories") categories: RequestBody,
        @Part product_image: MultipartBody.Part,
        @Part("purchase_price") purchasePrice: RequestBody,
        @Part("rent_price") rentPrice: RequestBody,
        @Part("rent_option") rentOption: RequestBody,
    ): Response<AddProductResponseModel>
}
