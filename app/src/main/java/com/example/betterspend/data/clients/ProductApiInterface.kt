package com.example.betterspend.data.clients

import com.example.betterspend.data.model.AddProductRequest
import com.example.betterspend.data.model.GenericResponse
import com.example.betterspend.data.model.UserProducts
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductApiInterface {
    @POST("/product")
    suspend fun addProduct(@Body addProductRequest: AddProductRequest) : GenericResponse

    @GET("/product")
    suspend fun getProducts(@Query("user") user : String) : UserProducts
}