package com.teebay.appname.features.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentLoginBinding
import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.viewModel.LoginViewModel
import com.teebay.appname.features.dashboard.DashboardActivity
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
                    (requireActivity() as AuthActivity).showMessage("Please fill all the input field")
                    return@setOnClickListener
                }
                viewModel.loginUser(LoginRequestModel(email = email, password = password, fcmToken = null))
            }

            tvSignup.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        }
    }

    private fun setObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Error -> {
                    hideLoader()
                    (requireActivity() as AuthActivity).showMessage(it.message)
                }
                ResponseState.Loading -> showLoader()
                is ResponseState.Success<*> -> {
                    hideLoader()
                    val data = it.data as? LoginRequestModel
                    data?.let { response ->
                        viewModel.saveCredentials(response)
                    }
                    (requireActivity() as AuthActivity).gotoDashboard()
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