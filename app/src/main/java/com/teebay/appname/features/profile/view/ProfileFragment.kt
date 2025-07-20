package com.teebay.appname.features.profile.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.teebay.appname.databinding.FragmentProfileBinding
import com.teebay.appname.features.auth.view.AuthActivity
import com.teebay.appname.features.profile.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfo()
        setObserver()
        binding?.btnLogout?.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

    private fun setObserver() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            binding?.apply {
                tvFname.text = it.firstName
                tvLname.text = it.lastName
                tvEmail.text = it.email
                tvAddress.text = it.address
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}