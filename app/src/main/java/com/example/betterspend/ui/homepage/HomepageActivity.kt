package com.example.betterspend.ui.homepage

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.betterspend.ui.common.AppNavigation
import com.example.betterspend.ui.common.NavigationBar
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.ProductViewmodel




class HomepageActivity : ComponentActivity() {
    private val productVM : ProductViewmodel by viewModels()
    private val userId = SharedPrefManager.getUserId().toString()

    // Define the ActivityResultLauncher for launching the ScannerActivity
    private val scanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedBarcode = result.data?.getStringExtra("SCANNED_BARCODE")
            if (!scannedBarcode.isNullOrEmpty()) {
                // Handle the scanned barcode, e.g., fetch product details using ViewModel
                Log.d("HomepageActivity", "Scanned Barcode: $scannedBarcode")
                productVM.fetchProductByBarcode(SharedPrefManager.getUserId().toString(), scannedBarcode)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Initialize the NavController
            val navController = rememberNavController()

            Scaffold(
                bottomBar = { NavigationBar(navController, scanLauncher) }
            ) { innerPadding ->
                AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding), productVM = productVM)
            }
        }
    }
}

@Composable
fun HomepageScreen(
    modifier:  Modifier,
    productVM: ProductViewmodel,
) {
    ItemList(modifier, productVM)
}