package com.example.betterspend.data.clients

import com.example.betterspend.data.model.LoginRequest
import com.example.betterspend.data.model.RegisterRequest
import com.example.betterspend.data.model.LoginResponse
import com.example.betterspend.data.model.GenericResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiInterface {
    @POST("/login")
    suspend fun login(@Body loginRequest : LoginRequest) : LoginResponse

    @POST("/register")
    suspend fun register(@Body registerRequest : RegisterRequest) : GenericResponse
}