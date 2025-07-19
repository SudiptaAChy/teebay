package com.teebay.appname.network

sealed class ResponseState<out T> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class Error(val message: String) : ResponseState<String>()
    data object Loading : ResponseState<Nothing>()
}