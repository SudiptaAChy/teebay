package com.teebay.appname.features.myProduct.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FormPagerAdapter(
    fragment: Fragment,
    private val children: List<Fragment>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = children.size

    override fun createFragment(position: Int): Fragment {
        return children[position]
    }
}