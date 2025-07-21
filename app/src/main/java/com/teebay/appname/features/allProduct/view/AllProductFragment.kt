package com.teebay.appname.features.allProduct.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentAllProductBinding
import com.teebay.appname.features.allProduct.adapter.ProductListAdapter
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.allProduct.viewModel.ProductViewModel
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProductFragment : Fragment() {
    private var binding: FragmentAllProductBinding? = null
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchAllProducts()
        viewModel.fetchACategories()
        setObserver()
    }

    private fun setObserver() {
        viewModel.productState.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    binding?.loader?.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResponseState.Loading -> {
                    binding?.apply {
                        rvProduct.visibility = View.GONE
                        loader.visibility = View.VISIBLE
                    }
                }
                is ResponseState.Success<*> -> {
                    binding?.loader?.visibility = View.GONE
                    binding?.rvProduct?.visibility = View.VISIBLE

                    val result = it.data as List<Product>
                    Log.d("sadsdfsdfdsf", "Result ::: $result")
                    val adapter = ProductListAdapter(result)
                    binding?.rvProduct?.adapter = adapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}