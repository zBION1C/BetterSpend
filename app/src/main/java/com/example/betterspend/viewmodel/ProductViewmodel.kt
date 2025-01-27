package com.example.betterspend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterspend.data.model.CategoriesDataFrame
import com.example.betterspend.data.model.Product
import com.example.betterspend.data.model.UserProducts
import com.example.betterspend.data.repository.BarcodeRepository
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
    val statistics: CategoriesDataFrame = CategoriesDataFrame(false, listOf()),
    var barcodeScanMessage: String = "",
    val errorMessage : String = ""
)

class ProductViewmodel() : ViewModel() {

    private val productRepo : ProductRepository = ProductRepository()
    private val barcodeRepo : BarcodeRepository = BarcodeRepository()

    private val _uiState = MutableStateFlow(HomepageUiState())
    val uiState : StateFlow<HomepageUiState> = _uiState.asStateFlow()

    private var fetchJob : Job? = null
    private var addJob : Job? = null

    fun addProduct(user : String, product : Product) {
        addJob?.cancel()
        addJob = viewModelScope.launch {
            try {
                productRepo.addProduct(user, product)
                _uiState.update {
                    it.copy(productItems = productRepo.getProducts(user))
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
                val productItems = productRepo.getProducts(user)
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

    fun fetchProductByBarcode(user: String, barcode : String) {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            try {
                val response = barcodeRepo.scan(barcode)

                if (response.success) {
                    addProduct(user, response.product)
                    _uiState.update {
                        it.copy(productItems = productRepo.getProducts(user))
                    }
                } else {
                    _uiState.update {
                        it.copy(barcodeScanMessage = response.message)
                    }
                }

            } catch (e: Exception) {
                // Handle the error and notify the UI when appropriate.
                _uiState.update {
                    it.copy(errorMessage = "An exception error occurred")
                }
            }
        }
    }

    fun fetchStatistics(user: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val response = productRepo.getCategories(user)

                if (response.success) {
                    _uiState.update {
                        it.copy(statistics = response)
                    }
                } else {
                    _uiState.update {
                        it.copy(errorMessage = response.success.toString())
                    }
                }

            } catch (e: Exception) {
                // Handle the error and notify the UI when appropriate.
                _uiState.update {
                    it.copy(errorMessage = "An exception error occurred")
                }
            }
        }
    }

}