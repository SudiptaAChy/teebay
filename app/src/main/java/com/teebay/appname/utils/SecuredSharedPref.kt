package com.teebay.appname.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Singleton
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Singleton
class SecuredSharedPref @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val pref =
        EncryptedSharedPreferences.create(
            "sharedPrefs",
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    fun put(key: String, value: String) = pref.edit { putString(key, value) }

    fun get(key: String, default: String? = null): String? = pref.getString(key, default)

    fun remove(key: String) = pref.edit { remove(key) }

    fun clear() = pref.edit { clear() }

    fun contains(key: String): Boolean = pref.contains(key)
}
