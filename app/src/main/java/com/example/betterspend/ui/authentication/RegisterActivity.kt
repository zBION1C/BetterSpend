package com.example.betterspend.ui.authentication


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterspend.viewmodel.RegisterState
import com.example.betterspend.viewmodel.UserViewmodel
import kotlin.getValue

class RegisterActivity : ComponentActivity() {

    // Pass the repository to AuthViewModelFactory
    private val viewModel: UserViewmodel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val registerState by viewModel.registerState.collectAsState()

            RegisterScreen(
                onRegisterSuccess = ::navigateToLoginPage,
                onRegister =  { email, password, confirmPassword ->
                    viewModel.register(email, password, confirmPassword) // Trigger login in ViewModel
                },
                registerState = registerState
            )
        }
    }

    fun navigateToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onRegister : (String, String, String) -> Unit,
    registerState : RegisterState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppIcon() // Reusing from LoginScreen
        Spacer(modifier = Modifier.height(40.dp))
        RegisterTitle()
        Spacer(modifier = Modifier.height(16.dp))
        EmailTextField(email, onValueChange = { email = it }) // Reusing from LoginScreen
        Spacer(modifier = Modifier.height(18.dp))
        PasswordTextField(
            password,
            onValueChange = { password = it }) // Reusing from LoginScreen
        Spacer(modifier = Modifier.height(18.dp))
        ConfirmPasswordTextField(confirmPassword, onValueChange = { confirmPassword = it })
        Spacer(modifier = Modifier.height(20.dp))
        RegisterButton(onClick = { onRegister(email, password, confirmPassword) })
        Spacer(modifier = Modifier.height(18.dp))

        when (registerState) {
            is RegisterState.Loading -> CircularProgressIndicator()
            is RegisterState.Error -> Text(
                text = registerState.message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )

            is RegisterState.Success -> {
                // Navigate to homepage on success (triggered in Activity)
                LaunchedEffect(Unit) {
                    onRegisterSuccess()
                }
            }

            else -> { /* Idle state, do nothing */
            }
        }
    }

}

@Composable
fun RegisterTitle() {
    Text(
        text = "Create a BetterSpend account",
        fontSize = 18.sp,
        color = Color.Black,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordTextField(confirmPassword: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = onValueChange,
        label = { Text("Confirm password") },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Gray
        )
    )
}

@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8E8FF)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "REGISTER", color = Color.Black)
    }
}
