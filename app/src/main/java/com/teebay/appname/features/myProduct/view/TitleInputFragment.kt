package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentTitleInputBinding
import com.teebay.appname.features.myProduct.viewModel.AddProductViewModel

class TitleInputFragment : Fragment() {
    private var binding: FragmentTitleInputBinding? = null
    private val viewModel: AddProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTitleInputBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.etTitle?.doAfterTextChanged {
            val title = it?.toString()?.trim()
            if (!title.isNullOrEmpty()) {
                viewModel.addTitle(title)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}