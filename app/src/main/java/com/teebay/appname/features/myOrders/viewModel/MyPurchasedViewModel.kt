package com.teebay.appname.features.myOrders.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teebay.appname.features.allProduct.model.Product
import com.teebay.appname.features.myOrders.repository.MyOrderRepository
import com.teebay.appname.features.myProduct.repository.ProductRepository
import com.teebay.appname.network.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyPurchasedViewModel @Inject constructor(
    private val orderRepository: MyOrderRepository,
    private val productRepository: ProductRepository
): ViewModel() {
    private val _state = MutableLiveData<ResponseState<List<Product>>>()
    val state: LiveData<ResponseState<List<Product>>> = _state

    fun fetchPurchasedProducts() {
        _state.value = ResponseState.Loading

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                orderRepository.fetchPurchasedProducts()
            }

            _state.value = result.fold(
                onSuccess = { response ->
                    val purchaseProducts = mutableListOf<Product>()
                    response.forEach { model ->
                        launch(Dispatchers.IO) {
                            val pid = model.product ?: return@launch
                            val product = productRepository.fetchProduct(pid)
                            product.onSuccess {
                                purchaseProducts.add(
                                    it.copy(datePosted = model.purchaseDate)
                                )
                            }
                        }
                    }
                    ResponseState.Success(purchaseProducts)
                },
                onFailure = { ResponseState.Error(it.message ?: "unknown error") }
            )
        }
    }
}