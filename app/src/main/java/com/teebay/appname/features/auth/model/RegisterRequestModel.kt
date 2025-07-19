package com.teebay.appname.features.auth.model

import com.google.gson.annotations.SerializedName

data class RegisterRequestModel(
    val address: String,
    val email: String,
    @SerializedName("firebase_console_manager_token")
    val firebaseConsoleManagerToken: String?,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val password: String
)