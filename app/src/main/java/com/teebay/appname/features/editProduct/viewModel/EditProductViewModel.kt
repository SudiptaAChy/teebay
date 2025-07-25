package com.teebay.appname.features.editProduct.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.features.myProduct.model.AddProductRequestModel
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.network.ResponseState
import com.teebay.appname.utils.SecuredSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject
import kotlin.text.toInt

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val pref: SecuredSharedPref
): ViewModel() {
    private val _product = MutableLiveData(AddProductRequestModel())

    private val _categoriesState = MutableLiveData<ResponseState<Any>>()
    val categoriesState: LiveData<ResponseState<Any>> = _categoriesState

    private val _deleteState = MutableLiveData<ResponseState<Any>>()
    val deleteState: LiveData<ResponseState<Any>> = _deleteState

    private val _updateState = MutableLiveData<ResponseState<Any>>()
    val updateState: LiveData<ResponseState<Any>> = _updateState

    fun fetchCategories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { repository.fetchCategories() }
            _categoriesState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun addTitle(value: String) {
        val newProduct = _product.value?.copy(title = value)
        newProduct?.let { _product.value = it }
    }

    fun addCategory(value: String) {
        val newProduct = _product.value?.copy(categories = value)
        newProduct?.let { _product.value = it }
    }

    fun addDescription(value: String) {
        val newProduct = _product.value?.copy(description = value)
        newProduct?.let { _product.value = it }
    }

    fun addPurchasePrice(value: String) {
        val newProduct = _product.value?.copy(purchasePrice = value)
        newProduct?.let { _product.value = it }
    }

    fun addRentPrice(value: String) {
        val newProduct = _product.value?.copy(rentPrice = value)
        newProduct?.let { _product.value = it }
    }

    fun addRentOption(value: String) {
        val newProduct = _product.value?.copy(rentOption = value)
        newProduct?.let { _product.value = it }
    }

    fun addImage(value: MultipartBody.Part) {
        val newProduct = _product.value?.copy(productImage = value)
        newProduct?.let { _product.value = it }
    }

    fun deleteProduct(id: Int?) {
        val id = id ?: return

        _deleteState.value = ResponseState.Loading

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.deleteProduct(id)
            }
            _deleteState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    // TODO: Fix empty pid problem
    fun updateProduct(
        pid: Int?,
    ) {
        val id = pref.get(PrefKeys.ID.name, null)?.toInt()
        val request = _product.value?.copy(seller = id?.toInt())
            ?: run {
                _updateState.value = ResponseState.Error("Some field is missing!")
                return
            }
        _product.value?.copy(id = pid)

        _updateState.value = ResponseState.Loading

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.updateProduct(request)
            }
            _updateState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }
}
