package com.teebay.appname.features.productDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.features.productDetails.model.PurchaseRequestModel
import com.teebay.appname.features.productDetails.model.RentRequestModel
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

    private val _rentState = MutableLiveData<ResponseState<Any>>()
    val rentState: LiveData<ResponseState<Any>> = _rentState

    private val _productState = MutableLiveData<ResponseState<Any>>()
    val productState: LiveData<ResponseState<Any>> = _productState

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

    fun rent(
        pid: Int?,
        option: String?,
        from: String,
        to: String
    ) {
        val pid = pid ?: return
        val uid = pref.get(PrefKeys.ID.name) ?: return
        val option = option ?: return

        _rentState.value = ResponseState.Loading

        viewModelScope.launch {
            val request = RentRequestModel(
                renter = uid.toInt(),
                product = pid,
                rentOption = option,
                startDate = from,
                endDate = to
            )
            val result = withContext(Dispatchers.IO) {
                repository.rent(request)
            }
            _rentState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun fetchProduct(pid: Int) {
        _productState.value = ResponseState.Loading

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.fetchProduct(pid)
            }
            _productState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }
}