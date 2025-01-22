package com.example.betterspend.ui.authentication.registration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.betterspend.viewmodel.UserViewmodel

class RegisterActivity : ComponentActivity() {
    private val userVM: UserViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegisterScreen(userVM)
        }
    }
}

@Composable
fun RegisterScreen(userVM: UserViewmodel) {
    Column ( modifier = Modifier
        .fillMaxSize()
        .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RegisterHeader(modifier = Modifier
            .weight(0.7f)
            .fillMaxWidth()
        )
        RegisterBody(
            userVM = userVM,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        RegisterFooter(modifier = Modifier
            .weight(0.2f)
            .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun RegisterPreview() {
    RegisterScreen(UserViewmodel())
}