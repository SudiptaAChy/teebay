package com.teebay.appname.di

import android.app.Application
import com.teebay.appname.network.FCMService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FCMService.createNotificationChannel(this)
    }
}
