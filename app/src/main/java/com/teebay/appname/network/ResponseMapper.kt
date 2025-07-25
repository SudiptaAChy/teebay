package com.teebay.appname.network

import com.google.gson.Gson
import com.teebay.appname.features.allProduct.model.Product
import retrofit2.Response

fun <T> Response<T>.mapResult(): Result<T> {
    if (this.isSuccessful) {
        val data = this.body()
        return if(data != null) Result.success(data)
        else {
            Result.success(EmptyResponseModel() as T)
        }
    } else {
        val errorResponse = this.parseError<ErrorResponseModel>()
        return Result.failure(Exception(errorResponse?.error))
    }
}

inline fun <reified E> Response<*>.parseError(): E? {
    return try {
        val errorJson = this.errorBody()?.string()
        if (errorJson != null) Gson().fromJson(errorJson, E::class.java) else null
    } catch (e: Exception) {
        null
    }
}

suspend fun <T> safeApiCall(
    block: suspend () -> Result<T>
): Result<T> {
    return try {
        block()
    } catch (e: Exception) {
        Result.failure(Exception(e.localizedMessage))
    }
}
