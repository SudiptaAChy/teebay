package com.teebay.appname.features.myProduct.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.teebay.appname.databinding.FragmentImageInputBinding
import com.teebay.appname.features.myProduct.viewModel.AddProductViewModel
import com.teebay.appname.utils.ImageUtil

class ImageInputFragment : Fragment() {
    private var binding: FragmentImageInputBinding? = null
    private val viewModel: AddProductViewModel by activityViewModels()

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding?.ivImage?.let { view ->
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
                Glide.with(this)
                    .load(it)
                    .into(view)
            }
            viewModel.addImage(ImageUtil.bitmapToMultipart(it))
        } ?: run {
            showMessage("No image captured")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageInputBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding?.btnCameraUpload?.setOnClickListener {
            checkCameraPermissionAndLaunch()
        }

        binding?.btnGalleryUpload?.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED -> {
                takePicturePreviewLauncher.launch(null)
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showMessage("Camera permission is needed to take photos")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
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
