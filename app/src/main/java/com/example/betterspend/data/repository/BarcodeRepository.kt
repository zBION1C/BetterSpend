package com.example.betterspend.data.repository

import com.example.betterspend.data.clients.RetrofitClient
import com.example.betterspend.data.model.Product
import com.example.betterspend.data.model.ScanResponse

class BarcodeRepository {
    suspend fun scan(upc : String) : ScanResponse {
        var fullProduct = RetrofitClient.barcodeApi.scan(upc)

        var response : ScanResponse

        if (fullProduct.items.isEmpty()) {
            response = ScanResponse(false, "Product not found", Product())
            return response
        }
        val product = Product(
            fullProduct.items[0].title,
            fullProduct.items[0].category,
            fullProduct.items[0].lowest_recorded_price,
            fullProduct.items[0].images[0]
        )

        response = ScanResponse(true, "Product found", product)
        return response
    }
}