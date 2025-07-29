package com.teebay.appname.features.myOrders.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.teebay.appname.databinding.FragmentMyPurchasedBinding
import com.teebay.appname.features.allProduct.adapter.ProductListAdapter
import com.teebay.appname.features.myOrders.viewModel.MyPurchasedViewModel
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPurchasedFragment : Fragment() {
    private var binding: FragmentMyPurchasedBinding? = null
    private val viewModel: MyPurchasedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPurchasedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchPurchasedProducts()

        setObserver()
    }

    private fun setObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    binding?.loader?.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResponseState.Loading -> {
                    binding?.apply {
                        rvProducts.visibility = View.GONE
                        loader.visibility = View.VISIBLE
                    }
                }
                is ResponseState.Success -> {
                    binding?.loader?.visibility = View.GONE

                    val products = it.data

                    if (products.isEmpty()) {
                        binding?.viewNoProduct?.root?.visibility = View.VISIBLE
                    } else {
                        binding?.rvProducts?.visibility = View.VISIBLE
                        binding?.rvProducts?.adapter = ProductListAdapter(products)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}