package com.teebay.appname.features.myProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teebay.appname.databinding.FragmentMyProductBinding

class MyProductFragment : Fragment() {
    private var binding: FragmentMyProductBinding? = null

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}