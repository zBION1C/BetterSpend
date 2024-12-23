package com.example.betterspend.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.VisualTransformation
import com.example.betterspend.R
import com.example.betterspend.data.authentication.SharedPrefManager
import com.example.betterspend.ui.homepage.HomepageActivity
import com.example.betterspend.ui.customComponents.MyButton
import com.example.betterspend.ui.customComponents.MyTextField
import com.example.betterspend.viewmodel.LoginState
import com.example.betterspend.viewmodel.UserViewmodel

class LoginActivity : ComponentActivity() {

    private val viewModel: UserViewmodel by viewModels()

    private lateinit var sharedPrefManager: SharedPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPrefManager
        sharedPrefManager = SharedPrefManager(this)

//        // Check if the user is already logged in
        if (sharedPrefManager.isLoggedIn()) {
            val userId = sharedPrefManager.getUserId()
            if (userId != null) {
                navigateToHomepage(userId)
            }
        }

        setContent {
            val loginState by viewModel.loginState.collectAsState() // Observe state

            // Handle navigation and show the UI
            LoginScreen(
                onRegister = ::onRegister,
                onLogin = { email, password ->
                    Log.d("MIAO", "${email},${password}")
                    viewModel.login(email, password) // Trigger login in ViewModel
                },
                loginState = loginState,
                onLoginSuccess = ::onLoginSuccess
            )
        }
    }

    private fun onRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHomepage(userId : String) {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onLoginSuccess(userId: String) {
        // Save login state
        sharedPrefManager.saveLoginState(userId)

        // Navigate to homepage
        navigateToHomepage(userId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onLogin: (String, String) -> Unit,
    loginState: LoginState,
    onLoginSuccess: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppIcon()
        Spacer(modifier = Modifier.height(40.dp))
        LoginTitle()
        Spacer(modifier = Modifier.height(16.dp))
        EmailTextField(email) { email = it }
        Spacer(modifier = Modifier.height(18.dp))
        PasswordTextField(password) { password = it }
        Spacer(modifier = Modifier.height(20.dp))
        LoginButton { onLogin(email, password) }
        Spacer(modifier = Modifier.height(16.dp))
        RegisterText(onRegister)

        when (loginState) {
            is LoginState.Loading -> CircularProgressIndicator()
            is LoginState.Error -> Text(
                text = loginState.message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
            is LoginState.Success -> {
                // Navigate to homepage on success (triggered in Activity)
                LaunchedEffect(Unit) {
                    val userId = loginState.response.id
                    onLoginSuccess(userId.toString())
                }
            }
            else -> { /* Idle state, do nothing */ }
        }
    }
}

@Composable
fun AppIcon() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Cart Icon",
        modifier = Modifier.size(100.dp)
    )
}

@Composable
fun LoginTitle() {
    Text(
        text = "Login",
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(email: String, onValueChange: (String) -> Unit) {
    MyTextField(
        value = email,
        onValueChange = onValueChange,
        label = "Enter email",
        transformation = VisualTransformation.None
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(password: String, onValueChange: (String) -> Unit) {
    MyTextField(
        value = password,
        onValueChange = onValueChange,
        label = "Enter password",
        transformation = PasswordVisualTransformation(),
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    MyButton(
        onClick = onClick,
        text = "Login"
    )
}

@Composable
fun RegisterText(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = "Or register to BetterSpend")
    }
}