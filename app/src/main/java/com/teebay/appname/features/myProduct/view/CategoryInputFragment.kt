package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentCategoryInputBinding
import com.teebay.appname.features.dashboard.DashboardActivity
import com.teebay.appname.features.myProduct.model.CategoryResponseModel
import com.teebay.appname.features.myProduct.viewModel.AddProductViewModel
import com.teebay.appname.network.ResponseState

class CategoryInputFragment : Fragment() {
    private var binding: FragmentCategoryInputBinding? = null
    private val viewModel: AddProductViewModel by activityViewModels()

    private lateinit var categories: List<CategoryResponseModel>

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

        viewModel.fetchCategories()

        viewModel.categoriesState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResponseState.Loading -> {}
                is ResponseState.Success<*> -> {
                    categories = it.data as List<CategoryResponseModel>
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

        binding?.ddCategory?.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            categories.forEach {
                if (it.label == selectedItem) {
                    viewModel.addCategory(it.value.toString())
                    return@forEach
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}