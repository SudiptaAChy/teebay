package com.teebay.appname

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.teebay.appname.features.auth.view.AuthActivity
import com.teebay.appname.features.dashboard.DashboardActivity
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.utils.SecuredSharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var pref: SecuredSharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = pref.get(PrefKeys.EMAIL.name, null)
        val password = pref.get(PrefKeys.PASSWORD.name, null)

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
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