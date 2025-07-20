package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentPriceInputBinding
import com.teebay.appname.features.myProduct.viewModel.AddProductViewModel

class PriceInputFragment : Fragment() {
    private var binding: FragmentPriceInputBinding? = null
    private val viewModel: AddProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPriceInputBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Per day", "Per hour")
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding?.ddOptions?.setAdapter(adapter)

        binding?.etPrice?.doAfterTextChanged {
            val price = it?.toString()?.trim()
            if (!price.isNullOrEmpty()) {
                viewModel.addPurchasePrice(price)
            }
        }

        binding?.etRent?.doAfterTextChanged {
            val rent = it?.toString()?.trim()
            if (!rent.isNullOrEmpty()) {
                viewModel.addRentPrice(rent)
            }
        }

        binding?.ddOptions?.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            viewModel.addRentOption(selectedItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}