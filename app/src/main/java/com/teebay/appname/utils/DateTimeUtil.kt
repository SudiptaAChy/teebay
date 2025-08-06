package com.teebay.appname.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDate(dateStr: String): String {
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = isoFormat.parse(dateStr)
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        "Invalid date"
    }
}


fun formatDateToISO(date: Date): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}

fun formatDateToUI(date: Date): String {
    val format = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    return format.format(date)
}

fun isFromDateBeforeToDate(fromDate: String, toDate: String): Boolean {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    val from = format.parse(fromDate) ?: return false
    val to = format.parse(toDate) ?: return false
    return from.before(to)
}

fun isBeforeDate(fromDate: String, toDate: String): Boolean {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val from = format.parse(fromDate)
        val to = format.parse(toDate)
        from != null && to != null && from.before(to)
    } catch (e: Exception) {
        false
    }
}
