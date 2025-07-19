package com.teebay.appname.features.auth.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teebay.appname.R
import com.teebay.appname.databinding.FragmentRegistrationBinding
import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.model.RegisterRequestModel
import com.teebay.appname.features.auth.model.RegisterResponseModel
import com.teebay.appname.features.auth.viewModel.RegistrationViewModel
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var binding: FragmentRegistrationBinding? = null
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        setObserver()
    }

    private fun setClickListener() {
        binding?.apply {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val firstName = etFirstName.text.toString().trim()
                val lastName = etLastName.text.toString().trim()
                val address = etAddress.text.toString().trim()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()
                if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() ||
                    email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    (requireActivity() as AuthActivity).showMessage("Please fill all the input field")
                    return@setOnClickListener
                }
                if (password != confirmPassword) {
                    (requireActivity() as AuthActivity).showMessage("Password should be match with confirm password")
                    return@setOnClickListener
                }
                viewModel.registerUser(
                    RegisterRequestModel(
                        firstName = firstName,
                        lastName = lastName,
                        address = address,
                        email = email,
                        password = password,
                        firebaseConsoleManagerToken = null
                    )
                )
            }

            tvSignin.setOnClickListener {
                findNavController().popBackStack()
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
                    val data = it.data as? RegisterResponseModel
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
        binding?.btnRegister?.visibility = View.INVISIBLE
    }

    private fun hideLoader() {
        binding?.loader?.visibility = View.GONE
        binding?.btnRegister?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}