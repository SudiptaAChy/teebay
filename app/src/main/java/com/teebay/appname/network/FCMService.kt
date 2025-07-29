package com.teebay.appname.network

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teebay.appname.R
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.features.notification.view.ProductDetailPendingActivity
import com.teebay.appname.utils.SecuredSharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    @Inject
    lateinit var pref: SecuredSharedPref

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: ""
        val body = remoteMessage.notification?.body ?: ""
        val productId = remoteMessage.data[PRODUCT_ID_TAG]
        showNotification(title, body, productId)
    }

    override fun onNewToken(token: String) {
        pref.put(PrefKeys.FCM.name, token)
    }

    private fun showNotification(
        title: String,
        message: String,
        productId: String?,
    ) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(this, ProductDetailPendingActivity::class.java).apply {
                putExtra(PRODUCT_ID_TAG, productId)
            }

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification =
                NotificationCompat
                    .Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

            NotificationManagerCompat.from(this).notify(1001, notification)
        }
    }

    companion object {
        const val CHANNEL_ID = "default_channel"
        const val CHANNEL_NAME = "General"
        const val PRODUCT_ID_TAG = "product_id"

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

                val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
