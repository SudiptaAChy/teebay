package com.teebay.appname.utils

fun String?.toMoneySign(): String {
    if(this == null) return ""
    return "$${this}"
}

fun String?.shortenText(maxLength: Int = 50): String {
    if(this == null) return ""
    return if (this.length > maxLength) this.take(maxLength) + "..." else this
}
