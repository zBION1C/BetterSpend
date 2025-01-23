package com.example.betterspend.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.betterspend.ui.homepage.HomepageActivity
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.UserViewmodel
import org.opencv.android.OpenCVLoader
import kotlin.getValue

class LoginActivity : ComponentActivity() {
    private val userVM: UserViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (!OpenCVLoader.initDebug())
            Log.e("OpenCV", "Unable to load OpenCV!");
        else
            Log.d("OpenCV", "OpenCV loaded Successfully!");


        // Initialize SharedPrefManager to hold global data like the logged user id and profile picture
        SharedPrefManager.initialize(applicationContext)

        // Check if the user is already logged in
        if (SharedPrefManager.isLoggedIn()) {
            val Intent = Intent(this, HomepageActivity::class.java)
            startActivity(Intent)
            finish()
        }

        setContent {
            LoginScreen(userVM)
        }
    }
}

@Composable
fun LoginScreen(userVM: UserViewmodel) {
    Column ( modifier = Modifier
        .fillMaxSize()
        .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        // Logo and Login text
        LoginHeader(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        )
        // Login form
        LoginBody(
            userVM = userVM,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        // Registration link
        LoginFooter(modifier = Modifier
            .weight(0.2f)
            .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    LoginScreen(userVM = UserViewmodel())
}