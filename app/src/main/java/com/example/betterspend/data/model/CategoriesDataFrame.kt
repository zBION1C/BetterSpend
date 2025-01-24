package com.example.betterspend.data.model

data class Category(
    val name: String,
    val frequency: Int
)

data class CategoriesDataFrame (
    val success: Boolean,
    val categories: List<Category>
)