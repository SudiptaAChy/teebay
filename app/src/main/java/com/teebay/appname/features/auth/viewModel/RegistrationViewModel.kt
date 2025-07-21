package com.teebay.appname.features.auth.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.auth.model.RegisterRequestModel
import com.teebay.appname.features.auth.model.RegisterResponseModel
import com.teebay.appname.features.auth.repository.AuthRepository
import com.teebay.appname.network.ResponseState
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.utils.SecuredSharedPref
import com.teebay.appname.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val securedPref: SecuredSharedPref,
    private val pref: SharedPref,
): ViewModel() {
    private val _state = MutableLiveData<ResponseState<Any>>()
    val state: LiveData<ResponseState<Any>> = _state

    fun registerUser(
        request: RegisterRequestModel
    ) {
        _state.value = ResponseState.Loading
        viewModelScope.launch {
            val result = repository.register(request)
            _state.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun saveCredentials(data: RegisterResponseModel) {
        data.id?.let { securedPref.put(PrefKeys.ID.name, it.toString()) }
        data.email?.let { securedPref.put(PrefKeys.EMAIL.name, it) }
        data.password?.let { securedPref.put(PrefKeys.PASSWORD.name, it) }
        data.firstName?.let { pref.put(PrefKeys.FNAME.name, it) }
        data.lastName?.let { pref.put(PrefKeys.LNAME.name, it) }
        data.address?.let { pref.put(PrefKeys.ADDRESS.name, it) }
    }
}