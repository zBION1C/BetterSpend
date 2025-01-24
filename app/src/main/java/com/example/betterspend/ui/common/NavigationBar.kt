package com.example.betterspend.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.betterspend.ui.authentication.login.LoginActivity
import com.example.betterspend.ui.homepage.HomepageActivity
import com.example.betterspend.ui.scanner.ScannerActivity
import com.example.betterspend.ui.statistics.StatisticsActivity
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.ProductViewmodel

fun navigateTo(
    context: Context,
    destination: String,
    scanLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val currentActivity = (context as? Activity)?.javaClass?.simpleName

    lateinit var intent: Intent

    val activityMap = mapOf(
        "Homepage" to HomepageActivity::class.java ,
        "Statistics" to StatisticsActivity::class.java,
        "Scan" to ScannerActivity::class.java,
        "Logout" to LoginActivity::class.java
    )

    // Get the corresponding activity class for the destination
    val activityClass = activityMap[destination] ?: return

    // Check if the current activity is already running
    if (activityClass.simpleName == currentActivity) {
        return
    }


    // Start the new activity
    intent = Intent(context, activityClass)

    if (destination == "Scan") {
        scanLauncher.launch(intent)
    } else {
        context.startActivity(intent)
    }

    if (destination == "Logout") {
        // Clear the saved login state
        SharedPrefManager.clearLoginState()
        if (context is Activity) {
            context.finish()
        }
    }

}

@Composable
fun NavigationBar(productVM: ProductViewmodel) {
    // Todo: FIX THE SELECTED LOGIC FOR ICONS
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Homepage", "Statistics", "Scan", "Logout")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.PieChart, Icons.Filled.QrCodeScanner, Icons.Filled.Logout)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.PieChart, Icons.Outlined.QrCodeScanner, Icons.Outlined.Logout)

    // Get the current context for navigation purposes
    val context = LocalContext.current

    // Define the ActivityResultLauncher for ScannerActivity
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedBarcode = result.data?.getStringExtra("SCANNED_BARCODE")
            if (!scannedBarcode.isNullOrEmpty()) {
                Log.d("MIAO", "Scanned Barcode: $scannedBarcode")
                Toast.makeText(context, "Barcode: $scannedBarcode", Toast.LENGTH_LONG).show()

                // Handle the scanned barcode (e.g., update ViewModel, navigate, etc.)
                productVM.fetchProductByBarcode(SharedPrefManager.getUserId().toString(), scannedBarcode.toString())
            }
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem (
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navigateTo(context, item, scanLauncher) }
            )
        }
    }
}