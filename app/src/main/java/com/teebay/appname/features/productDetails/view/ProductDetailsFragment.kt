package com.teebay.appname.features.productDetails.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teebay.appname.databinding.FragmentProductDetailsBinding
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.utils.toMoneySign
import com.teebay.appname.R
import com.teebay.appname.databinding.DialogDatePickerBinding
import com.teebay.appname.features.productDetails.viewModel.ProductDetailsViewModel
import com.teebay.appname.network.ResponseState
import com.teebay.appname.utils.formatDateToISO
import com.teebay.appname.utils.formatDateToUI
import com.teebay.appname.utils.isFromDateBeforeToDate
import com.teebay.appname.utils.showCustomDialog
import com.teebay.appname.utils.showLoaderDialog
import com.teebay.appname.utils.showSimpleDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private var binding: FragmentProductDetailsBinding? = null
    private val args : ProductDetailsFragmentArgs by navArgs()
    private var product: Product? = null
    private val viewModel: ProductDetailsViewModel by viewModels()
    private val loadingDialog: AlertDialog by lazy {
        requireContext().showLoaderDialog(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = args.product
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setListener()
        setObservable()
    }

    private fun setUI() {
        binding?.apply {
            tvTitle.text = product?.title ?: ""
            tvCategory.text = product?.categories?.first() ?: ""
            tvPurchasePrice.text = product?.purchasePrice.toMoneySign()
            tvRentPrice.text = product?.rentPrice.toMoneySign()
            tvRentOption.text = getString(R.string.rent_option_display, product?.rentOption ?: "")
            tvDescription.text = product?.description ?: ""
            Glide.with(requireContext())
                .load(product?.productImage)
                .error(R.drawable.ic_image_error)
                .into(ivProduct)
        }
    }

    private fun setListener() {
        binding?.apply {
            btnBuy.setOnClickListener { showBuyDialog() }

            btnRent.setOnClickListener { showRentDialog() }
        }
    }

    private fun setObservable() {
        viewModel.purchaseState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    hideLoaderDialog()
                    showMessage(it.message)
                }
                ResponseState.Loading -> {
                    showLoaderDialog()
                }
                is ResponseState.Success<*> -> {
                    hideLoaderDialog()
                    showMessage("Product purchased successfully")
                }
            }
        }

        viewModel.rentState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    hideLoaderDialog()
                    showMessage(it.message)
                }
                ResponseState.Loading -> {
                    showLoaderDialog()
                }
                is ResponseState.Success<*> -> {
                    hideLoaderDialog()
                    showMessage("Product rented successfully")
                }
            }
        }
    }

    private fun showBuyDialog() {
        requireContext().showSimpleDialog(
            message = "Are you sure you want to buy this product?",
            onPositiveClick = { viewModel.purchase(product?.id) }
        )
    }

    private fun showRentDialog() {
        val dialogBinding = DialogDatePickerBinding.inflate(layoutInflater)

        requireContext()
            .showCustomDialog(
                view = dialogBinding.root,
                onViewCreated = {
                    val dateSetListener = { editText: EditText ->
                        val calendar = Calendar.getInstance()
                        val datePicker = DatePickerDialog(
                            requireContext(),
                            { _, year, month, day ->
                                calendar.set(year, month, day)

                                val timePicker = TimePickerDialog(
                                    requireContext(),
                                    { _, hour, minute ->
                                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                                        calendar.set(Calendar.MINUTE, minute)
                                        calendar.set(Calendar.SECOND, 0)
                                        calendar.set(Calendar.MILLISECOND, 0)

                                        val date = calendar.time

                                        editText.setText(formatDateToUI(date))
                                        editText.tag = formatDateToISO(date)
                                    },
                                    calendar.get(Calendar.HOUR),
                                    calendar.get(Calendar.MINUTE),
                                    true
                                )

                                timePicker.show()
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePicker.show()
                    }

                    dialogBinding.etToDate.setOnClickListener { dateSetListener(dialogBinding.etToDate) }
                    dialogBinding.etFromDate.setOnClickListener { dateSetListener(dialogBinding.etFromDate) }
                },
                positiveBtnLabel = "Confirm Rent",
                negativeBtnLabel = "Back",
                onPositiveClick = {
                    val fromDate = dialogBinding.etFromDate.tag as? String
                    val toDate = dialogBinding.etToDate.tag as? String

                    if (fromDate != null && toDate != null) {
                        if (isFromDateBeforeToDate(fromDate, toDate)) {
                            viewModel.rent(product?.id, product?.rentOption, fromDate, toDate)
                        } else {
                            showMessage("From date should before to date")
                        }
                    } else {
                        showMessage("Please fill all date")
                    }
                },
            )
            .show()
    }

    private fun showLoaderDialog() {
        loadingDialog.show()
    }

    private fun hideLoaderDialog() {
        loadingDialog.dismiss()
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
