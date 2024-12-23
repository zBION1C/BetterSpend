package com.example.betterspend.data.model

data class ScanResponse(
    val success : Boolean = false,
    val message : String,
    val product : Product
)