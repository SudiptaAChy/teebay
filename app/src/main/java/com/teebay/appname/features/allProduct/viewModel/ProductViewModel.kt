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
    private val _productState = MutableLiveData<ResponseState<Any>>()
    val productState: LiveData<ResponseState<Any>> = _productState

    private val _categoriesState = MutableLiveData<ResponseState<Any>>()
    val categoriesState: LiveData<ResponseState<Any>> = _categoriesState

    private var allProducts = listOf<Product>()
    private val selectedCategories = mutableListOf<Category>()

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
        val uid = pref.get(PrefKeys.ID.name, null)?.toInt() ?: Int.MIN_VALUE
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { repository.fetchAllProducts() }
            _productState.value = result.fold(
                onSuccess = {
                    allProducts = it
                    val otherProducts = it.filter { it.seller != uid }
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

        if (selectedCategories.isEmpty()) return allProducts

        val filteredProducts = mutableListOf<Product>()
        selectedCategories.forEach { selected ->
            allProducts.forEach { item ->
                if (selected.value == item.categories?.first()) {
                    filteredProducts.add(item)
                }
            }
        }

        return filteredProducts
    }

    fun fetchMyProducts(): List<Product> {
        val id = pref.get(PrefKeys.ID.name, null)?.toInt() ?: return emptyList()
        return allProducts.filter { it.seller == id }
    }
}
