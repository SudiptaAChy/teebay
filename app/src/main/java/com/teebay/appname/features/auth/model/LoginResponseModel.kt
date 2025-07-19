package com.teebay.appname.features.auth.model

import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    val message: String?,
    val user: User?
)

data class User(
    val address: String?,
    @SerializedName("date_joined")
    val dateJoined: String?,
    val email: String?,
    @SerializedName("firebase_console_manager_token")
    val firebaseConsoleManagerToken: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    val id: Int?,
    val password: String?
)