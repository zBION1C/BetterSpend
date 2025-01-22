package com.example.betterspend.data.model

import android.net.Uri

data class LoginResponse(
    val success : Boolean,
    val message : String,
    val id : Int,
    val email : String,
    val image : Uri
)