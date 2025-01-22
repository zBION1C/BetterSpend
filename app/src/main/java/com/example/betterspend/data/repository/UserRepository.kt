package com.example.betterspend.data.repository

import android.net.Uri
import com.example.betterspend.data.clients.RetrofitClient
import com.example.betterspend.data.model.GenericResponse
import com.example.betterspend.data.model.LoginRequest
import com.example.betterspend.data.model.LoginResponse
import com.example.betterspend.data.model.RegisterRequest

class UserRepository() {
    suspend fun login(email : String, password : String) : LoginResponse {
        var response : LoginResponse
        if (email == "" || password == "") {
            response = LoginResponse(false, "All fields have to be filled", -1, "", Uri.EMPTY)
            return response
        }
        val request = LoginRequest(email, password)
        response = RetrofitClient.userApi.login(request)
        return response
    }

    suspend fun register(email: String, password: String, confirmationPassword: String, imageUri: Uri?) : GenericResponse {
        var response : GenericResponse

        if  (password != confirmationPassword) {
            response = GenericResponse(false, "Passwords do not match!")
            return response
        }
        if (email == "" || password == "" || confirmationPassword == "") {
            response = GenericResponse(false, "All fields have to be filled")
            return response
        }
        val request = RegisterRequest(email, password, imageUri.toString())
        response =  RetrofitClient.userApi.register(request)
        return response
    }
}