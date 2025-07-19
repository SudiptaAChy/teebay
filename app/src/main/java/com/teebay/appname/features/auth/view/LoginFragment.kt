package com.teebay.appname.features.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentLoginBinding
import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.viewModel.LoginViewModel
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        setObserver()
    }

    private fun setClickListener() {
        binding?.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all the input field", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.loginUser(email, password)
            }

            tvSignup.setOnClickListener {

            }
        }
    }

    private fun setObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResponseState.Loading -> showLoader()
                is ResponseState.Success<*> -> {
                    hideLoader()
                    Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoader() {
        binding?.loader?.visibility = View.VISIBLE
        binding?.btnLogin?.visibility = View.INVISIBLE
    }

    private fun hideLoader() {
        binding?.loader?.visibility = View.GONE
        binding?.btnLogin?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}