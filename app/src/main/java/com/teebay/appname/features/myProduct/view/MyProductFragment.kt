package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teebay.appname.databinding.FragmentMyProductBinding
import com.teebay.appname.features.allProduct.adapter.ProductListAdapter
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.allProduct.view.AllProductFragmentDirections
import com.teebay.appname.features.allProduct.viewModel.ProductViewModel
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MyProductFragment : Fragment() {
    private var binding: FragmentMyProductBinding? = null
    private val viewModel: ProductViewModel by activityViewModels()
    private lateinit var productAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnAdd?.setOnClickListener {
            AddProductFragment().show(parentFragmentManager, null)
        }

        val products = viewModel.fetchMyProducts()

        if (products.isEmpty()) {
            binding?.viewNoProduct?.root?.visibility = View.VISIBLE
        } else {
            productAdapter = ProductListAdapter(products) { index ->
                val product = products[index]
                val action =
                    MyProductFragmentDirections.actionMiMyProductToEditProductFragment(product)
                findNavController().navigate(action)
            }
            binding?.rvProduct?.adapter = productAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}