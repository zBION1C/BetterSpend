package com.example.betterspend.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

object SharedPrefManager {

    private const val PREFS_NAME = "user_prefs"
    private lateinit var sharedPreferences: SharedPreferences


    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Save login state
    fun saveLoginState(userId: String, userEmail: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", true)
        editor.putString("user_id", userId)
        editor.putString("user_email", userEmail)
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

    // Get user email
    fun getUserEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }

    // Clear login state
    fun clearLoginState() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveProfilePictureUri(uri: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("profile_picture_uri", uri)
        editor.apply()
    }

    fun getProfilePictureUri(): Uri? {
        val uriString = sharedPreferences.getString("profile_picture_uri", null)
        return uriString?.let { Uri.parse(it) }
    }
}
