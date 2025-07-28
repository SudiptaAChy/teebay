package com.teebay.appname.features.myOrders.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.teebay.appname.databinding.FragmentMyOrdersBinding
import com.teebay.appname.features.myProduct.adapter.FormPagerAdapter

class MyOrdersFragment : Fragment() {
    private var binding: FragmentMyOrdersBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val pagerFragments = listOf(MyPurchasedFragment(), MyRentFragment())
        binding?.apply {
            pager.adapter = FormPagerAdapter(this@MyOrdersFragment, pagerFragments)
            TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = when(position) {
                    0 -> "Purchased"
                    1 -> "Rent"
                    else -> ""
                }
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}