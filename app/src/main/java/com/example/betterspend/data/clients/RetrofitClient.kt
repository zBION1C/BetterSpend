package com.example.betterspend.data.clients

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_BETTERSPEND = "https://zBION1C.pythonanywhere.com/"
    private const val BASE_URL_BARCODE = "https://api.upcitemdb.com/prod/trial/"

    val userApi : UserApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_BETTERSPEND)
            .addConverterFactory(GsonConverterFactory.create()) // JSON Converter
            .build()
            .create(UserApiInterface::class.java)
    }

    val productApi : ProductApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_BETTERSPEND)
            .addConverterFactory(GsonConverterFactory.create()) // JSON Converter
            .build()
            .create(ProductApiInterface::class.java)
    }

    val barcodeApi : BarcodeApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_BARCODE)
            .addConverterFactory(GsonConverterFactory.create()) // JSON Converter
            .build()
            .create(BarcodeApiInterface::class.java)
    }


}