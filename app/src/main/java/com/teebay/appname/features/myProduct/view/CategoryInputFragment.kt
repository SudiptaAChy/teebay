package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentCategoryInputBinding
import com.teebay.appname.features.myProduct.viewModel.AddProductViewModel

class CategoryInputFragment : Fragment() {
    private var binding: FragmentCategoryInputBinding? = null
    private val viewModel: AddProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryInputBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Electronics", "Toys", "Cars")
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding?.ddCategory?.setAdapter(adapter)

        binding?.ddCategory?.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            viewModel.addCategory(selectedItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}