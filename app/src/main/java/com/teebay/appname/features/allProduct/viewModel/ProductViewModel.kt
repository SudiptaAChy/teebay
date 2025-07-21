package com.teebay.appname.features.allProduct.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
): ViewModel() {
    private val _productState = MutableLiveData<ResponseState<Any>>()
    val productState: LiveData<ResponseState<Any>> = _productState

    private val _categoriesState = MutableLiveData<ResponseState<Any>>()
    val categoriesState: LiveData<ResponseState<Any>> = _categoriesState

    fun fetchACategories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { repository.fetchCategories() }
            _categoriesState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun fetchAllProducts() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { repository.fetchAllProducts() }
            _productState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }
}