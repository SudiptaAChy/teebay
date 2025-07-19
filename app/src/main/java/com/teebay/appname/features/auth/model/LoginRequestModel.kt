package com.teebay.appname.features.auth.model

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    val email: String,
    val password: String,
    @SerializedName("fcm_token")
    val fcmToken: String?,
)