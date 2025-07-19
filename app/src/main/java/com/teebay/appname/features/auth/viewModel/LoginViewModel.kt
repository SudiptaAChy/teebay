package com.teebay.appname.features.auth.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.repository.AuthRepository
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _state = MutableLiveData<ResponseState<Any>>()
    val state: LiveData<ResponseState<Any>> = _state

    fun loginUser(email: String, password: String) {
        _state.value = ResponseState.Loading
        viewModelScope.launch {
            val request = LoginRequestModel(email = email, password = password, fcmToken = null)
            val result = repository.login(request)
            _state.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }
}