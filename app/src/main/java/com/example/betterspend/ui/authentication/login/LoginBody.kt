package com.example.betterspend.ui.authentication.login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.betterspend.ui.common.EmailTextField
import com.example.betterspend.ui.common.PasswordTextField
import com.example.betterspend.ui.homepage.HomepageActivity
import com.example.betterspend.viewmodel.LoginState
import com.example.betterspend.viewmodel.UserViewmodel

@Composable
fun LoginBody(userVM: UserViewmodel, modifier: Modifier) {
    val loginState by userVM.loginState.collectAsState() // Observe state
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EmailTextField(
            email = email,
            onEmailChange = { email = it }
        )
        PasswordTextField(
            password = password,
            onPasswordChange = { password = it}
        )
        LoginButton(
            email = email,
            password = password,
            userVM = userVM
        )

        when (loginState) {
            is LoginState.Loading -> CircularProgressIndicator()
            is LoginState.Error -> Text(
                text = (loginState as LoginState.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
            is LoginState.Success -> {
                // Navigate to homepage on success (triggered in Activity)
                LaunchedEffect(Unit) {
                    val intent = Intent(context, HomepageActivity::class.java)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.finish()
                    }
                }
            }
            else -> { /* Idle state, do nothing */ }
        }
    }
}

@Composable
fun LoginButton(email: String, password: String, userVM: UserViewmodel) {
    Button(
        onClick = { userVM.login(email, password)},
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(5.dp), // Adjust the corner radius as needed
        contentPadding = PaddingValues(0.dp)
    ) {
        Text("Login")
    }
}

@Preview
@Composable
fun LoginBodyPreview() {
    LoginBody(UserViewmodel(), Modifier)
}