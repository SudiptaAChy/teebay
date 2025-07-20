package com.teebay.appname.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPref @Inject constructor(
    @ApplicationContext context: Context
) {
    private val pref: SharedPreferences =
        context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

    fun put(key: String, value: String) = pref.edit { putString(key, value) }

    fun get(key: String, default: String? = null): String? = pref.getString(key, default)

    fun remove(key: String) = pref.edit { remove(key) }

    fun clear() = pref.edit { clear() }

    fun contains(key: String): Boolean = pref.contains(key)
}
