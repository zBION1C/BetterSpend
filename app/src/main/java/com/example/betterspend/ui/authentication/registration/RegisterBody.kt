package com.example.betterspend.ui.authentication.registration

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.betterspend.ui.authentication.login.LoginActivity
import com.example.betterspend.ui.common.EmailTextField
import com.example.betterspend.ui.common.PasswordTextField
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.RegisterState
import com.example.betterspend.viewmodel.UserViewmodel


@Composable
fun RegisterBody(userVM: UserViewmodel, modifier: Modifier) {
    val registerState by userVM.registerState.collectAsState() // Observe state
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //TODO: Save uri in SharedPrefManager
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    Column (
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
            onPasswordChange = { password = it }
        )
        PasswordTextField(password = confirmPassword,
            onPasswordChange = { confirmPassword = it }
        )
        RegisterButton(
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            imageUri = imageUri,
            userVM = userVM
        )
        ImagePicker(
            onImageSelected = { uri -> imageUri = uri }
        )

        when (registerState) {
            is RegisterState.Loading -> CircularProgressIndicator()
            is RegisterState.Error -> Text(
                text = (registerState as RegisterState.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )

            is RegisterState.Success -> {
                // Login on success to redirect user to homepage after registration
                LaunchedEffect(Unit) {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            } else -> { /* Idle state, do nothing */  }
        }

    }
}

@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Button(
        onClick = { launcher.launch("image/*") },
    ) {
        Text("Select profile picture")
    }
}

@Composable
fun RegisterButton(email: String, password: String, confirmPassword: String, imageUri: Uri?, userVM: UserViewmodel) {
    Button(
        onClick = { userVM.register(email, password, confirmPassword, imageUri)},
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(5.dp), // Adjust the corner radius as needed
        contentPadding = PaddingValues(0.dp)
    ) {
        Text("Register")
    }
}