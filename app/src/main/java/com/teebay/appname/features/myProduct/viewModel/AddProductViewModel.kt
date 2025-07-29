package com.teebay.appname.features.myProduct.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.myProduct.model.AddProductRequestModel
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.network.ResponseState
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import com.teebay.appname.features.myProduct.model.Category
import com.teebay.appname.utils.SecuredSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val securedPref: SecuredSharedPref,
    private val repository: ProductRepository
): ViewModel() {
    private val _product = MutableLiveData(AddProductRequestModel())
    val product: LiveData<AddProductRequestModel> = _product

    private val _productSummary = MutableLiveData<String>()
    val productSummary: LiveData<String> = _productSummary

    private val _productState = MutableLiveData<ResponseState<AddProductResponseModel>>()
    val productState: LiveData<ResponseState<AddProductResponseModel>> = _productState

    private val _categoriesState = MutableLiveData<ResponseState<List<Category>>>()
    val categoriesState: LiveData<ResponseState<List<Category>>> = _categoriesState

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
        _productSummary.value = getSummary()
    }

    fun addRentPrice(value: String) {
        val newProduct = _product.value?.copy(rentPrice = value)
        newProduct?.let { _product.value = it }
        _productSummary.value = getSummary()
    }

    fun addRentOption(value: String) {
        val newProduct = _product.value?.copy(rentOption = value)
        newProduct?.let { _product.value = it }
        _productSummary.value = getSummary()
    }

    fun addImage(value: MultipartBody.Part) {
        val newProduct = _product.value?.copy(productImage = value)
        newProduct?.let { _product.value = it }
    }

    private fun getSummary(): String {
        return """
            Title: ${_product.value?.title}
            Category: ${_product.value?.categories}
            Description: ${_product.value?.description}
            Price: ${_product.value?.purchasePrice}
            To rent: ${_product.value?.rentPrice} (${_product.value?.rentOption})
        """.trimIndent()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { repository.fetchCategories() }
            _categoriesState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun submitProduct() {
        val id = securedPref.get(PrefKeys.ID.name, null)?.toInt()
        val request = _product.value?.copy(seller = id?.toInt())
            ?: run {
                _productState.value = ResponseState.Error("Some field is missing!")
                return
            }

        _productState.value = ResponseState.Loading

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.postProduct(request)
            }
            _productState.value = result.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }
}
