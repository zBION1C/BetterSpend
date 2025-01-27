package com.example.betterspend.data.repository

import android.util.Log
import com.example.betterspend.data.clients.RetrofitClient
import com.example.betterspend.data.model.AddProductRequest
import com.example.betterspend.data.model.CategoriesDataFrame
import com.example.betterspend.data.model.GenericResponse
import com.example.betterspend.data.model.Product
import com.example.betterspend.data.model.UserProducts

class ProductRepository {
    suspend fun addProduct(user : String, product : Product) : GenericResponse {
        val request = AddProductRequest(user, product)
        val response = RetrofitClient.productApi.addProduct(request)
        return response
    }

    suspend fun getProducts(user : String) : UserProducts {
        val response = RetrofitClient.productApi.getProducts(user)
        return response
    }

    suspend fun getCategories(user : String) : CategoriesDataFrame {
        val response = RetrofitClient.productApi.getCategories(user)
        return response
    }
}