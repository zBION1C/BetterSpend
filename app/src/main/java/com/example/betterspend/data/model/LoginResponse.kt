package com.example.betterspend.data.model

data class LoginResponse(
    val success : Boolean,
    val message : String,
    val id : Int,
    val email : String
)