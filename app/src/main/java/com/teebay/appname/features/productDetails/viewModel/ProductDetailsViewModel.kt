package com.teebay.appname.features.productDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.features.productDetails.model.PurchaseRequestModel
import com.teebay.appname.features.productDetails.model.PurchaseResponseModel
import com.teebay.appname.network.ResponseState
import com.teebay.appname.utils.SecuredSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val pref: SecuredSharedPref
): ViewModel() {
    private val _purchaseState = MutableLiveData<ResponseState<Any>>()
    val purchaseState: LiveData<ResponseState<Any>> = _purchaseState

    fun purchase(pid: Int?) {
        val pid = pid ?: return
        val uid = pref.get(PrefKeys.ID.name) ?: return

        _purchaseState.value = ResponseState.Loading

        viewModelScope.launch {
            val request = PurchaseRequestModel(pid, uid.toInt())
            val result = withContext(Dispatchers.IO) {
                repository.purchase(request)
            }
            _purchaseState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun rent() {}
}