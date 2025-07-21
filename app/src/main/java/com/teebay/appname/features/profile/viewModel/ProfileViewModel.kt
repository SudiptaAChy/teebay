package com.teebay.appname.features.profile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teebay.appname.features.auth.model.User
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.utils.SecuredSharedPref
import com.teebay.appname.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val securedPref: SecuredSharedPref,
    private val pref: SharedPref
): ViewModel() {
    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User> = _userInfo

    fun getUserInfo() {
        val email = securedPref.get(PrefKeys.EMAIL.name, null) ?: ""
        val fName = pref.get(PrefKeys.FNAME.name, null) ?: ""
        val lName = pref.get(PrefKeys.LNAME.name, null) ?: ""
        val address = pref.get(PrefKeys.ADDRESS.name, null) ?: ""

        _userInfo.value = User(
            firstName = fName,
            lastName = lName,
            email = email,
            address = address,
            dateJoined = null,
            id = null,
            firebaseConsoleManagerToken = null,
            password = null
        )
    }

    fun logout() {
        securedPref.clear()
        pref.clear()
    }
}