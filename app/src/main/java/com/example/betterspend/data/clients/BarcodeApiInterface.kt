package com.example.betterspend.data.clients

import com.example.betterspend.data.model.ProductApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface BarcodeApiInterface {
    @GET("lookup")
    suspend fun scan(@Query("upc") upc : String) : ProductApiModel
}