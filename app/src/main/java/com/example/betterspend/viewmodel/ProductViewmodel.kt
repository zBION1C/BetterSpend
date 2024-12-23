package com.example.betterspend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterspend.data.model.Product
import com.example.betterspend.data.model.UserProducts
import com.example.betterspend.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


data class HomepageUiState(
    val productItems : UserProducts = UserProducts(listOf()),
    val errorMessage : String = ""
)

class ProductViewmodel() : ViewModel() {

    private val repository : ProductRepository = ProductRepository()
    private val _uiState = MutableStateFlow(HomepageUiState())
    val uiState : StateFlow<HomepageUiState> = _uiState.asStateFlow()

    private var fetchJob : Job? = null
    private var addJob : Job? = null

    fun addProduct(user : String, product : Product) {
        addJob?.cancel()
        addJob = viewModelScope.launch {
            try {
                repository.addProduct(user, product)
                _uiState.update {
                    it.copy(productItems = repository.getProducts(user))
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Product not added")
                }
            }
        }
    }

    fun fetchProducts(user : String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val productItems = repository.getProducts(user)
                _uiState.update {
                    it.copy(productItems = productItems)
                }
            } catch (ioe : IOException) {
                // Handle the error and notify the UI when appropriate.
                _uiState.update {
                    it.copy(errorMessage = "An exception error occurred")
                }
            }
        }
    }

}