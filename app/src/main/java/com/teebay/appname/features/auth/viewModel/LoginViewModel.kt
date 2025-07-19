package com.teebay.appname.features.auth.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.repository.AuthRepository
import com.teebay.appname.network.ResponseState
import com.teebay.appname.utils.PrefKeys
import com.teebay.appname.utils.SecuredSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val pref: SecuredSharedPref
): ViewModel() {
    private val _state = MutableLiveData<ResponseState<Any>>()
    val state: LiveData<ResponseState<Any>> = _state

    fun loginUser(request: LoginRequestModel) {
        _state.value = ResponseState.Loading
        viewModelScope.launch {
            val result = repository.login(request)
            _state.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun saveCredentials(data: LoginRequestModel) {
        data.email?.let { pref.put(PrefKeys.EMAIL.name, it) }
        data.password?.let { pref.put(PrefKeys.PASSWORD.name, it) }
    }
}