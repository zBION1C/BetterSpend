package com.example.betterspend.ui.authentication.login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.betterspend.ui.authentication.registration.RegisterActivity
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginFooter(modifier: Modifier) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        RegistrationText()
    }
}


@Composable
fun RegistrationText() {
    val context = LocalContext.current
    TextButton(
        onClick = {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }
    ) {
        Text("Don't have an account? Register here.")
    }
}


@Preview
@Composable
fun RegistrationTextPreview() {
    LoginFooter(Modifier)
}