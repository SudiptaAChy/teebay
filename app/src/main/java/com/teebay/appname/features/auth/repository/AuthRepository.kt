package com.teebay.appname.features.auth.repository

import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.model.LoginResponseModel
import com.teebay.appname.features.auth.model.RegisterRequestModel
import com.teebay.appname.features.auth.model.RegisterResponseModel
import com.teebay.appname.features.auth.service.AuthApiService
import com.teebay.appname.network.mapResult
import com.teebay.appname.network.safeApiCall
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApiService
) {
    suspend fun login(request: LoginRequestModel): Result<LoginResponseModel> =
        safeApiCall { apiService.login(request).mapResult() }

    suspend fun register(request: RegisterRequestModel): Result<RegisterResponseModel> =
        safeApiCall { apiService.register(request).mapResult() }
}