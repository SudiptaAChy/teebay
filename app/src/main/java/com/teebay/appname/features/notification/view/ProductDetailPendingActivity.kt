package com.teebay.appname.features.notification.view

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.teebay.appname.R
import com.teebay.appname.databinding.ActivityProductDetailPendingBinding
import com.teebay.appname.features.allProduct.adapter.ProductListAdapter
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.allProduct.view.AllProductFragmentDirections
import com.teebay.appname.features.productDetails.viewModel.ProductDetailsViewModel
import com.teebay.appname.network.FCMService
import com.teebay.appname.network.ResponseState
import com.teebay.appname.utils.formatDate
import com.teebay.appname.utils.toMoneySign
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailPendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailPendingBinding
    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductDetailPendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusBarColor)
        }

        initUI()
        getData()
        setObserver()
    }

    private fun initUI() {
        binding.viewProductDetails.apply {
            btnRent.visibility = View.GONE
            btnBuy.visibility = View.GONE
        }
    }

    private fun getData() {
        intent.getStringExtra(FCMService.PRODUCT_ID_TAG)?.let {
            val productId = it.toInt()
            viewModel.fetchProduct(productId)
        }
    }

    private fun setObserver() {
        viewModel.productState.observe(this) {
            when(it) {
                is ResponseState.Error -> {
                    binding.loader.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                ResponseState.Loading -> {
                    binding.apply {
                        viewProductDetails.root.visibility = View.GONE
                        loader.visibility = View.VISIBLE
                    }
                }
                is ResponseState.Success -> {
                    binding.apply {
                        loader.visibility = View.GONE
                        viewProductDetails.root.visibility = View.VISIBLE
                    }

                    val product = it.data

                    binding.viewProductDetails.apply {
                        tvTitle.text = product.title ?: ""
                        tvCategory.text = product.categories?.first() ?: ""
                        tvPurchasePrice.text = product.purchasePrice.toMoneySign()
                        tvRentPrice.text = product.rentPrice.toMoneySign()
                        tvRentOption.text = getString(R.string.rent_option_display, product.rentOption ?: "")
                        tvDescription.text = product.description ?: ""
                        tvDate.text = formatDate(product.datePosted.toString())
                        Glide.with(this@ProductDetailPendingActivity)
                            .load(product.productImage)
                            .error(R.drawable.ic_image_error)
                            .into(ivProduct)
                    }
                }
            }
        }
    }
}
