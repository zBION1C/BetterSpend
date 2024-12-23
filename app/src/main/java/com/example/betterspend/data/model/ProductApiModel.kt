package com.example.betterspend.data.model


data class ProductApiModel(
    val code: String,
    val total: Int,
    val offset: Int,
    val items: List<ProductItem>
)

data class ProductItem(
    val ean: String,
    val title: String,
    val description: String,
    val brand: String,
    val model: String,
    val color: String,
    val size: String?,
    val dimension: String,
    val weight: String,
    val category: String,
    val lowest_recorded_price: Double,
    val highest_recorded_price: Double,
    val images: List<String>,
    val offers: List<Offer>,
    val asin: String,
    val elid: String
)

data class Offer(
    val merchant: String,
    val domain: String,
    val title: String,
    val currency: String,
    val listPrice: Double,
    val price: Double,
    val shipping: String,
    val condition: String,
    val availability: String,
    val link: String,
    val updatedT: Long
)
