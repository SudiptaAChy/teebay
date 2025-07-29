package com.teebay.appname.features.editProduct.view

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teebay.appname.constants.RentOption
import com.teebay.appname.databinding.FragmentEditProductBinding
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.editProduct.viewModel.EditProductViewModel
import com.teebay.appname.features.myProduct.model.Category
import com.teebay.appname.network.ResponseState
import com.teebay.appname.utils.ImageUtil
import com.teebay.appname.utils.showCustomDialog
import com.teebay.appname.utils.showLoaderDialog
import com.teebay.appname.utils.showOptionDialog
import com.teebay.appname.utils.showSimpleDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    private var binding: FragmentEditProductBinding? = null
    private val args : EditProductFragmentArgs by navArgs()
    private var product: Product? = null
    private lateinit var categories: List<Category>
    private val viewModel: EditProductViewModel by viewModels()
    private val loadingDialog: AlertDialog by lazy {
        requireContext().showLoaderDialog(requireActivity())
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding?.ivImage?.let { view ->
                view.visibility = View.VISIBLE
                Glide.with(this)
                    .load(it)
                    .into(view)
            }
            ImageUtil.uriToMultiPart(it)?.also {
                viewModel.addImage(it)
            }
        } ?: run {
            showMessage("No image selected")
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            takePicturePreviewLauncher.launch(null)
        } else {
            showMessage("Camera permission denied")
        }
    }

    private val takePicturePreviewLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            binding?.ivImage?.let { view ->
                view.visibility = View.VISIBLE
                Glide.with(this)
                    .load(it)
                    .into(view)
            }
            viewModel.addImage(ImageUtil.bitmapToMultipart(it))
        } ?: run {
            showMessage("No image captured")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = args.product
        viewModel.updateRequestModel(product)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCategories()
        setUI()
        setObservable()
        setListener()
    }

    private fun setUI() {
        binding?.apply {
            etTitle.setText(product?.title ?: "")
            ddCategory.setText(product?.categories?.first(), true)
            etDescription.setText(product?.description ?: "")
            etPrice.setText(product?.purchasePrice ?: "")
            etRent.setText(product?.rentPrice ?: "")
            try {
                ddOptions.setText(RentOption.valueOf(product?.rentOption ?: "").title)
            } catch (e: IllegalArgumentException) {
                Log.e("Exception", e.localizedMessage)
            }
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(RentOption.hour.title, RentOption.day.title)
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding?.ddOptions?.setAdapter(adapter)
    }

    private fun setObservable() {
        viewModel.categoriesState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    showMessage(it.message)
                }
                ResponseState.Loading -> {}
                is ResponseState.Success -> {
                    categories = it.data
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categories.map { it.label }
                    ).also {
                        it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding?.ddCategory?.setAdapter(it)
                    }
                }
            }
        }

        viewModel.deleteState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    loadingDialog.dismiss()
                    showMessage(it.message)
                }
                ResponseState.Loading -> { loadingDialog.show() }
                is ResponseState.Success -> {
                    loadingDialog.dismiss()
                    showMessage("Product deleted successfully")
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.updateState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    loadingDialog.dismiss()
                    showMessage(it.message)
                }
                ResponseState.Loading -> { loadingDialog.show() }
                is ResponseState.Success -> {
                    loadingDialog.dismiss()
                    showMessage("Product updated successfully")
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setListener() {
        binding?.apply {
            etTitle.doAfterTextChanged {
                val title = it?.toString()?.trim()
                if (!title.isNullOrEmpty()) {
                    viewModel.addTitle(title)
                }
            }

            ddCategory.setOnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                categories.forEach {
                    if (it.label == selectedItem) {
                        viewModel.addCategory(it.value.toString())
                        return@forEach
                    }
                }
            }

            etDescription.doAfterTextChanged {
                val description = it?.toString()?.trim()
                if (!description.isNullOrEmpty()) {
                    viewModel.addDescription(description)
                }
            }

            btnUpload.setOnClickListener {
                requireContext()
                    .showOptionDialog(
                        title = "Select Image Source",
                        options = arrayOf("Upload from Camera", "Upload from Gallery"),
                        onItemClick = {
                            when (it) {
                                0 -> { checkCameraPermissionAndLaunch() }
                                1 -> { galleryLauncher.launch("image/*") }
                            }
                        }
                    )
            }

            etPrice.doAfterTextChanged {
                val price = it?.toString()?.trim()
                if (!price.isNullOrEmpty()) {
                    viewModel.addPurchasePrice(price)
                }
            }

            etRent.doAfterTextChanged {
                val rent = it?.toString()?.trim()
                if (!rent.isNullOrEmpty()) {
                    viewModel.addRentPrice(rent)
                }
            }

            ddOptions.setOnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                for (option in RentOption.entries) {
                    if (option.title == selectedItem) {
                        viewModel.addRentOption(option.name)
                        break
                    }
                }
            }

            btnDelete.setOnClickListener {
                requireContext()
                    .showSimpleDialog(
                        message = "Are you sure to delete this product?",
                        onPositiveClick = { viewModel.deleteProduct(product?.id) }
                    )
            }

            btnUpdate.setOnClickListener { viewModel.updateProduct() }
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED) {
            takePicturePreviewLauncher.launch(null)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
