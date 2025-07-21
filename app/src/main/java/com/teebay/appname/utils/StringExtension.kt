package com.teebay.appname.utils

fun String?.toMoneySign(): String {
    if(this == null) return ""
    return "$${this}"
}