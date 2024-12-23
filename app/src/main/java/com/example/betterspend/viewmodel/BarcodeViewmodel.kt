package com.example.betterspend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterspend.data.model.Product
import com.example.betterspend.data.repository.BarcodeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BarcodeScannerUiState(
    val product : Product = Product(),
    val message : String? = null
)

class BarcodeViewmodel : ViewModel()  {
    private val repository : BarcodeRepository = BarcodeRepository()
    private val _uiState = MutableStateFlow(BarcodeScannerUiState())
    val uiState : StateFlow<BarcodeScannerUiState> = _uiState.asStateFlow()

    private var fetchJob : Job? = null

    fun fetchProductByBarcode(barcode : String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val response = repository.scan(barcode)

                if (response.success) {
                    _uiState.update {
                        it.copy(product = response.product, message = response.message)
                    }
                } else {
                    _uiState.update {
                        it.copy(message = response.message)
                    }
                }

            } catch (e: Exception) {
                // Handle the error and notify the UI when appropriate.
                _uiState.update {
                    it.copy(message = "An exception error occurred")
                }
            }
        }
    }

    fun clearState() {
        _uiState.update {
            it.copy(
                product = Product(),
                message = ""
            )
        }
    }
}