package com.teebay.appname.features.myProduct.repository

import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.db.CategoryDao
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.myProduct.model.AddProductRequestModel
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import com.teebay.appname.features.myProduct.model.Category
import com.teebay.appname.features.myProduct.service.ProductApiService
import com.teebay.appname.features.productDetails.model.PurchaseRequestModel
import com.teebay.appname.features.productDetails.model.PurchaseResponseModel
import com.teebay.appname.features.productDetails.model.RentRequestModel
import com.teebay.appname.features.productDetails.model.RentResponseModel
import com.teebay.appname.network.EmptyResponseModel
import com.teebay.appname.network.mapResult
import com.teebay.appname.network.safeApiCall
import com.teebay.appname.utils.SharedPref
import com.teebay.appname.utils.isBeforeDate
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Date
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ProductApiService,
    private val dao: CategoryDao,
    private val pref: SharedPref,
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

    suspend fun fetchCategories(): Result<List<Category>> {
        val lastSync = pref.get(PrefKeys.LAST_SYNCED.name) ?: ""
        val needSync = isBeforeDate(lastSync, Date().toString())

        if(needSync) {
            val result = safeApiCall { apiService.fetchCategories().mapResult() }
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    dao.insertAll(it)
                }
            }
            return result
        } else {
            val result = dao.getAll()
            return Result.success(result)
        }
    }

    suspend fun fetchAllProducts(): Result<List<Product>> =
        safeApiCall { apiService.fetchAllProducts().mapResult() }

    suspend fun fetchProduct(id: Int): Result<Product> =
        safeApiCall { apiService.fetchProduct(id).mapResult() }

    suspend fun purchase(request: PurchaseRequestModel): Result<PurchaseResponseModel> =
        safeApiCall { apiService.purchase(request).mapResult() }

    suspend fun rent(request: RentRequestModel): Result<RentResponseModel> =
        safeApiCall { apiService.rent(request).mapResult() }

    suspend fun deleteProduct(id: Int): Result<EmptyResponseModel> =
        safeApiCall { apiService.deleteProduct(id).mapResult() }

    suspend fun updateProduct(
        request: AddProductRequestModel
    ): Result<AddProductResponseModel> {
        val pid = request.id ?: return Result.failure(Exception("Can't find product id"))
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
        val image = request.productImage

        return safeApiCall {
            apiService
                .updateProduct(
                    pid,
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
}
