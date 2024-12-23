package com.example.betterspend.data.authentication

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Save login state
    fun saveLoginState(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", true)
        editor.putString("user_id", userId)
        editor.apply()
    }

    // Get login state
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    // Get user ID
    fun getUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }

    // Clear login state
    fun clearLoginState() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
