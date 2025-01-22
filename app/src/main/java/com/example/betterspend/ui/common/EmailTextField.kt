package com.example.betterspend.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    var isError by remember { mutableStateOf(false) }

    // Email TextField
    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
        },
        placeholder = { Text("example@domain.com") },
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon"
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    )

    // Error message
    if (isError) {
        Text(
            text = "Invalid email address",
            color = Color.Red,
            modifier = Modifier
                .padding(start = 16.dp, top = 4.dp)
        )
    }
}