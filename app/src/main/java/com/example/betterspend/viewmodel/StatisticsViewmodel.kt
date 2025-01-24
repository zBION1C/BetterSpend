package com.example.betterspend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterspend.data.model.CategoriesDataFrame
import com.example.betterspend.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GraphState(
    val statistics: CategoriesDataFrame = CategoriesDataFrame(false, listOf()),
    val message : String = ""
)

class StatisticsViewmodel() : ViewModel() {
    private val productRepo : ProductRepository = ProductRepository()

    private val _uiState = MutableStateFlow(GraphState())
    val uiState : StateFlow<GraphState> = _uiState.asStateFlow()


    private var fetchJob : Job? = null

    //TODO: Implement this same login in another viewmodel called StatisticsViewmodel
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
                        it.copy(message = response.success.toString())
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

}