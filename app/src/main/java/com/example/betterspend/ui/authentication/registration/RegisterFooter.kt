package com.example.betterspend.ui.authentication.registration

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.betterspend.ui.authentication.login.LoginActivity

@Composable
fun RegisterFooter(modifier: Modifier) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom)
    {
        LoginText()
    }

}


@Composable
fun LoginText() {
    val context = LocalContext.current
    TextButton(
        onClick = {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }
    ) {
        Text("Already have an account? Login here.")
    }
}
