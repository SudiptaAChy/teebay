package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentDescriptionInputBinding
import com.teebay.appname.features.myProduct.viewModel.AddProductViewModel

class DescriptionInputFragment : Fragment() {
    private var binding: FragmentDescriptionInputBinding? = null
    private val viewModel: AddProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDescriptionInputBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.etDescription?.doAfterTextChanged {
            val description = it?.toString()?.trim()
            if (!description.isNullOrEmpty()) {
                viewModel.addDescription(description)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}