package com.teebay.appname

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.teebay.appname.features.auth.view.AuthActivity
import com.teebay.appname.features.dashboard.DashboardActivity
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.utils.SecuredSharedPref
import com.teebay.appname.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var securedPref: SecuredSharedPref
    @Inject
    lateinit var pref: SharedPref

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "You need to accept permission to receive notifications", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }

        fetchFCMToken()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                fetchFCMToken()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            fetchFCMToken()
        }
    }

    private fun fetchFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show()
                return@addOnCompleteListener
            }

            val token = task.result

            securedPref.put(PrefKeys.FCM.name, token)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestPermission()
        initUI()
    }

    private fun initUI() {
        val fName = securedPref.get(PrefKeys.FNAME.name, null)
        val lName = securedPref.get(PrefKeys.LNAME.name, null)
        val address = securedPref.get(PrefKeys.ADDRESS.name, null)

        if (fName==null && lName==null && address==null) {
            Intent(this, AuthActivity::class.java).also {
                startActivity(it)
                finish()
            }
        } else {
            Intent(this, DashboardActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}