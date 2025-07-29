package com.teebay.appname.features.allProduct.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.constants.PrefKeys
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.myProduct.model.Category
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.network.ResponseState
import com.teebay.appname.utils.SecuredSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val pref: SecuredSharedPref
): ViewModel() {
    private val _productState = MutableLiveData<ResponseState<List<Product>>>()
    val productState: LiveData<ResponseState<List<Product>>> = _productState

    private val _categoriesState = MutableLiveData<ResponseState<List<Category>>>()
    val categoriesState: LiveData<ResponseState<List<Category>>> = _categoriesState

    private var allProducts = listOf<Product>()
    private val selectedCategories = mutableListOf<Category>()

    private var userId= pref.get(PrefKeys.ID.name, null)?.toInt() ?: Int.MIN_VALUE

    fun fetchCategories() {
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
                onSuccess = {
                    allProducts = it
                    val otherProducts = it.filter { it.seller != userId }
                    ResponseState.Success(otherProducts)
                },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }

    fun onCategorySelected(category: Category): List<Product> {
        if (category.isSelected) {
            selectedCategories.add(category)
        } else {
            selectedCategories.remove(category)
        }

        val otherProducts = allProducts.filter { it.seller != userId }
        if (selectedCategories.isEmpty()) return otherProducts

        val filteredProducts = mutableListOf<Product>()
        selectedCategories.forEach { selected ->
            otherProducts.forEach { item ->
                if (selected.value == item.categories?.first()) {
                    filteredProducts.add(item)
                }
            }
        }

        return filteredProducts
    }

    fun fetchMyProducts(): List<Product> {
        return allProducts.filter { it.seller == userId }
    }
}
