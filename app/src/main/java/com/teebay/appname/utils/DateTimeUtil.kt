package com.teebay.appname.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateStr: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateStr)
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        "Invalid date"
    }
}
