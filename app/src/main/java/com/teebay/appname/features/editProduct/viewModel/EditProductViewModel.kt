package com.teebay.appname.features.editProduct.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.myProduct.model.AddProductRequestModel
import com.teebay.appname.features.myProduct.model.AddProductResponseModel
import com.teebay.appname.features.myProduct.model.Category
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.network.EmptyResponseModel
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val repository: ProductRepository,
): ViewModel() {
    private val _product = MutableLiveData(AddProductRequestModel())

    private val _categoriesState = MutableLiveData<ResponseState<List<Category>>>()
    val categoriesState: LiveData<ResponseState<List<Category>>> = _categoriesState

    private val _deleteState = MutableLiveData<ResponseState<EmptyResponseModel>>()
    val deleteState: LiveData<ResponseState<EmptyResponseModel>> = _deleteState

    private val _updateState = MutableLiveData<ResponseState<AddProductResponseModel>>()
    val updateState: LiveData<ResponseState<AddProductResponseModel>> = _updateState

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

    fun updateRequestModel(request: Product?) {
        request?.let {
            val newProduct = _product.value?.copy(
                id = it.id,
                seller = it.seller,
                title = it.title,
                description = it.description,
                categories = it.categories?.first(),
                purchasePrice = it.purchasePrice,
                rentPrice = it.rentPrice,
                rentOption = it.rentOption,
            )
            newProduct?.let { _product.value = it }
        }
    }

    fun updateProduct() {
        val request = _product.value ?: run {
            _updateState.value = ResponseState.Error("Invalid request")
            return
        }
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
