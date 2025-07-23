package com.teebay.appname.features.myProduct.repository

import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.myProduct.model.AddProductRequestModel
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import com.teebay.appname.features.myProduct.model.Category
import com.teebay.appname.features.myProduct.service.ProductApiService
import com.teebay.appname.features.productDetails.model.PurchaseRequestModel
import com.teebay.appname.features.productDetails.model.PurchaseResponseModel
import com.teebay.appname.features.productDetails.model.RentRequestModel
import com.teebay.appname.features.productDetails.model.RentResponseModel
import com.teebay.appname.network.mapResult
import com.teebay.appname.network.safeApiCall
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ProductApiService
) {
    suspend fun postProduct(request: AddProductRequestModel): Result<AddProductResponseModel> {
        val seller =
            request.seller?.toString()?.toRequestBody("text/plain".toMediaType())
                ?: return Result.failure(Exception("seller id is empty"))
        val title =
            request.title?.toRequestBody("text/plain".toMediaType()) ?: return Result.failure(Exception("title is empty"))
        val description =
            request.description?.toRequestBody("text/plain".toMediaType()) ?: return Result.failure(Exception("description is empty"))
        val purchasePrice = request.purchasePrice.toString().toRequestBody("text/plain".toMediaType())
        val rentPrice =
            request.rentPrice?.toString()?.toRequestBody("text/plain".toMediaType())
                ?: return Result.failure(Exception("rent price is empty"))
        val rentOption =
            request.rentOption?.toRequestBody("text/plain".toMediaType()) ?: return Result.failure(Exception("rent option is empty"))
        val categories =
            request.categories?.toRequestBody("text/plain".toMediaType()) ?: return Result.failure(Exception("category is empty"))
        val image = request.productImage ?: return Result.failure(Exception("select an image"))

        return safeApiCall {
            apiService
                .postProduct(
                    seller,
                    title,
                    description,
                    categories,
                    image,
                    purchasePrice,
                    rentPrice,
                    rentOption,).mapResult()
        }
    }

    suspend fun fetchCategories(): Result<List<Category>> =
        safeApiCall { apiService.fetchCategories().mapResult() }

    suspend fun fetchAllProducts(): Result<List<Product>> =
        safeApiCall { apiService.fetchtAllProducts().mapResult() }

    suspend fun purchase(request: PurchaseRequestModel): Result<PurchaseResponseModel> =
        safeApiCall { apiService.purchase(request).mapResult() }

    suspend fun rent(request: RentRequestModel): Result<RentResponseModel> =
        safeApiCall { apiService.rent(request).mapResult() }
}
