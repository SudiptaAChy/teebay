package com.teebay.appname.features.auth.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.auth.model.LoginRequestModel
import com.teebay.appname.features.auth.model.LoginResponseModel
import com.teebay.appname.features.auth.repository.AuthRepository
import com.teebay.appname.network.ResponseState
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.utils.SecuredSharedPref
import com.teebay.appname.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val securedPref: SecuredSharedPref,
    private val pref: SharedPref,
): ViewModel() {
    private val _state = MutableLiveData<ResponseState<Any>>()
    val state: LiveData<ResponseState<Any>> = _state

    fun loginUser(email: String, password: String) {
        val fcm = securedPref.get(PrefKeys.FCM.name, null)
        val request = LoginRequestModel(email = email, password = password, fcmToken = fcm)
        _state.value = ResponseState.Loading
        viewModelScope.launch {
            val result = repository.login(request)
            _state.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun loginInBiometric() {
        val email = securedPref.get(PrefKeys.EMAIL.name) ?: return
        val password = securedPref.get(PrefKeys.PASSWORD.name) ?: return
        loginUser(email, password)
    }

    fun showBiometric(): Boolean {
        return securedPref.contains(PrefKeys.EMAIL.name) && securedPref.contains(PrefKeys.PASSWORD.name)
    }

    fun saveCredentials(data: LoginResponseModel) {
        data.user?.let { user ->
            user.id?.let { securedPref.put(PrefKeys.ID.name, it.toString()) }
            user.email?.let { securedPref.put(PrefKeys.EMAIL.name, it) }
            user.password?.let { securedPref.put(PrefKeys.PASSWORD.name, it) }
            user.firstName?.let { pref.put(PrefKeys.FNAME.name, it) }
            user.lastName?.let { pref.put(PrefKeys.LNAME.name, it) }
            user.address?.let { pref.put(PrefKeys.ADDRESS.name, it) }
        }
    }
}
