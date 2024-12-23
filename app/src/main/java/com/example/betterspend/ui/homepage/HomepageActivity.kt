package com.example.betterspend.ui.homepage

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.runtime.Composable
import com.example.betterspend.data.authentication.SharedPrefManager
import com.example.betterspend.data.model.Product
import com.example.betterspend.ui.authentication.LoginActivity
import com.example.betterspend.ui.scanner.ScannerActivity
import com.example.betterspend.viewmodel.BarcodeViewmodel
import com.example.betterspend.viewmodel.ProductViewmodel

class HomepageActivity : AppCompatActivity() {
    private var userId = ""
    private val viewmodel: ProductViewmodel by viewModels()
    private val barcodeViewmodel: BarcodeViewmodel by viewModels()

    // Initialize SharedPrefManager
    private lateinit var sharedPrefManager: SharedPrefManager


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        sharedPrefManager = SharedPrefManager(this)

        // Check if the user is logged in
        val userId = sharedPrefManager.getUserId()

        if (userId == null) {
            // If not logged in, navigate to login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close HomepageActivity
        } else {
            // If logged in, continue with the existing flow
            this.userId = userId
            viewmodel.fetchProducts(userId) // Fetch user-specific products
            setContent {
                GroceryActivityScreen(
                    onBarcodeFound = { product ->
                        lifecycleScope.launch {
                            Log.d("MIAO", "$product")
                            viewmodel.addProduct(userId, product)
                        }
                    },
                    viewModel = viewmodel,
                    barcodeViewmodel = barcodeViewmodel,
                    onExit = ::onExitButtonClicked
                )
            }
        }
    }

    // Handle the "Exit" functionality, which logs the user out
    private fun logout() {
        // Clear the saved login state
        sharedPrefManager.clearLoginState()

        // Navigate back to the LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    fun onExitButtonClicked() {
        logout()  // Call logout to clear session and navigate to login
    }
}

@Composable
fun GroceryActivityScreen(
    onBarcodeFound: (Product) -> Unit,
    viewModel: ProductViewmodel,
    barcodeViewmodel : BarcodeViewmodel,
    onExit: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F0))  // Background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top bar with exit button
            GroceryTopBar(onExit)

            Spacer(modifier = Modifier.height(20.dp))

            // LazyColumn to display products
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.value.productItems.products.size) { index ->
                    val product = uiState.value.productItems.products[index]
                    ProductCard(
                        productName = product.title,
                        price = "$${product.price}",
                        imageUrl = product.img
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom buttons (e.g., barcode scanner button)
            BottomButtons(onBarcodeFound, barcodeViewmodel)
        }

        if (uiState.value.errorMessage != "") {
            // Show error overlay if there's an error
            CenteredErrorDisplay(
                error = uiState.value.errorMessage,
                onDismiss = { }  // Dismiss error after some time
            )
        }
    }
}

@Composable
fun CenteredErrorDisplay(
    error: String?,
    onDismiss: () -> Unit
) {
    if (error != null) {
        // Wait for 5 seconds before dismissing the error
        LaunchedEffect(error) {
            delay(5000L)  // Wait for 5 seconds
            onDismiss()  // Dismiss the error
        }

        // Display error message in an overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(24.dp)
            ) {
                Text(
                    text = error,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun GroceryTopBar(onExit : () -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Home Icon
            IconButton(onClick = { /* Handle Home Click */ }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Stats Icon
            IconButton(onClick = { /* Handle Stats Click */ }) {
                Icon(
                    imageVector = Icons.Default.Addchart,
                    contentDescription = "Stats",
                    tint = Color.Black
                )
            }
        }

        // Exit Button
        TextButton(
            onClick = { onExit() }
        ) {
            Text(
                text = "Exit",
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ProductCard(productName: String, price: String, imageUrl: String? = null) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            // If image URL is provided, load the image
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = productName,
                    modifier = Modifier
                        .width(180.dp)
                        .height(180.dp) // Adjust the image height as needed
                )
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                // Display the product name and price
                Text(
                    text = productName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = price,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BottomButtons(
    onBarcodeFound: (Product) -> Unit,
    barcodeViewmodel: BarcodeViewmodel
) {
    // Collect the state from the ViewModel
    val uiState by barcodeViewmodel.uiState.collectAsState()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedBarcode = result.data?.getStringExtra("SCANNED_BARCODE")
            if (scannedBarcode != null) {
                // Process the barcode
                Toast.makeText(context, "Scanned: ${scannedBarcode}", Toast.LENGTH_SHORT).show()
                barcodeViewmodel.fetchProductByBarcode(scannedBarcode)
            }
        }
    }

    // Observe the product state and call the callback when product data is available
    if (uiState.product.title.isNotEmpty()) {
        onBarcodeFound(uiState.product)
        barcodeViewmodel.clearState()
    }

    Button(
        onClick = {
            val intent = Intent(context, ScannerActivity::class.java)
            launcher.launch(intent)  // Launch the barcode scanner
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8E8FF)),  // Light blue button
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
    ) {
        Text(
            text = "Scan product",
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
