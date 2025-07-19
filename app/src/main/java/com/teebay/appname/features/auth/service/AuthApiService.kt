package com.teebay.appname.features.auth.service

import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.model.RegisterRequestModel
import com.teebay.appname.features.auth.model.RegisterResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("users/login/")
    suspend fun login(@Body request: LoginRequestModel): Response<LoginRequestModel>

    @POST("users/register/")
    suspend fun register(@Body request: RegisterRequestModel): Response<RegisterResponseModel>
}