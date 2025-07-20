package com.teebay.appname.features.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.teebay.appname.R
import com.teebay.appname.databinding.ActivityDashboardBinding
import com.teebay.appname.features.allProduct.AllProductFragment
import com.teebay.appname.features.myProduct.MyProductFragment
import com.teebay.appname.features.profile.view.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private val allProductFragment = AllProductFragment()
    private val myProductFragment = MyProductFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        showFragment(allProductFragment)

        binding.bottomNav.setOnItemSelectedListener {
            val fragment = when(it.itemId) {
                R.id.miAllProduct -> allProductFragment
                R.id.miMyProduct -> myProductFragment
                R.id.miProfile -> profileFragment
                else -> null
            }

            fragment?.let { showFragment(it) }

            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragContainer, fragment)
            .commit()
    }
}