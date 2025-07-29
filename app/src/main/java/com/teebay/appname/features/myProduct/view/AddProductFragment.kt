package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentAddProductBinding
import com.teebay.appname.features.myProduct.adapter.FormPagerAdapter
import com.teebay.appname.features.myProduct.viewModel.AddProductViewModel
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.platform.PlatformRegistry.applicationContext

@AndroidEntryPoint
class AddProductFragment : BottomSheetDialogFragment() {
    private var binding: FragmentAddProductBinding? = null
    private val pagerFragments =
        listOf(
            TitleInputFragment(),
            CategoryInputFragment(),
            DescriptionInputFragment(),
            ImageInputFragment(),
            PriceInputFragment(),
            InputSummaryFragment(),
        )
    private lateinit var adapter: FormPagerAdapter
    private val viewModel: AddProductViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                it.post {
                    val screenHeight = resources.displayMetrics.heightPixels.toFloat()
                    it.translationY = screenHeight
                    it
                        .animate()
                        .translationY(0f)
                        .setDuration(600L)
                        .start()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setView()
        setObserver()
    }

    private fun setView() {
        adapter = FormPagerAdapter(this, pagerFragments)
        binding?.pager?.adapter = adapter

        binding?.pager?.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding?.slider?.progress = (position + 1) * 100 / adapter.itemCount

                    if (position == pagerFragments.size - 1) {
                        binding?.apply {
                            btnSubmit.visibility = View.VISIBLE
                            btnNext.visibility = View.GONE
                        }
                    } else {
                        binding?.apply {
                            btnSubmit.visibility = View.GONE
                            btnNext.visibility = View.VISIBLE
                        }
                    }
                }
            },
        )
    }

    private fun setObserver() {
        viewModel.productState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    showAlert(it.message)
                    binding?.btnSubmit?.isEnabled = true
                }
                ResponseState.Loading -> {
                    binding?.btnSubmit?.isEnabled = false
                }
                is ResponseState.Success -> {
                    showAlert("Product added successfully!")
                    binding?.btnSubmit?.isEnabled = true
                    dismiss()
                }
            }
        }
    }

    private fun setOnClickListener() {
        binding?.btnPrev?.setOnClickListener {
            val prevItem = binding?.pager?.currentItem?.minus(1)
            prevItem?.let {
                if (it >= 0) {
                    binding?.pager?.currentItem = it
                }
            }
        }

        binding?.btnNext?.setOnClickListener {
            val index = binding?.pager?.currentItem
            val canNavigate = when(index) {
                0 -> {
                    if (viewModel.product.value?.title.isNullOrEmpty()) {
                        showAlert("Please fill the title")
                        false
                    } else {
                        true
                    }
                }
                1 -> {
                    if (viewModel.product.value?.categories.isNullOrEmpty()) {
                        showAlert("Please fill the category")
                        false
                    } else {
                        true
                    }
                }
                2 -> {
                    if (viewModel.product.value?.description.isNullOrEmpty()) {
                        showAlert("Please fill the description")
                        false
                    } else {
                        true
                    }
                }
                3 -> {
                    if (viewModel.product.value?.productImage == null) {
                        showAlert("Please upload an image")
                        false
                    } else {
                        true
                    }
                }
                4 -> {
                    if (viewModel.product.value?.purchasePrice.isNullOrEmpty() ||
                        viewModel.product.value?.rentPrice.isNullOrEmpty() ||
                        viewModel.product.value?.rentOption.isNullOrEmpty()) {
                        showAlert("Please fill all fields")
                        false
                    } else {
                        true
                    }
                }
                else -> false
            }

            if (canNavigate) {
                val nextItem = binding?.pager?.currentItem?.plus(1)
                nextItem?.let {
                    if (it < adapter.itemCount) {
                        binding?.pager?.currentItem = it
                    }
                }
            }
        }

        binding?.btnCross?.setOnClickListener { dismiss() }

        binding?.btnSubmit?.setOnClickListener {
            viewModel.submitProduct()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showAlert(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
