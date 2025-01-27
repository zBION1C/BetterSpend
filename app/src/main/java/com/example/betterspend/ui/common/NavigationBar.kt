package com.example.betterspend.ui.common

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.betterspend.ui.authentication.login.LoginActivity
import com.example.betterspend.ui.homepage.HomepageScreen
import com.example.betterspend.ui.scanner.ScannerActivity
import com.example.betterspend.ui.statistics.StatisticsScreen
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.ProductViewmodel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier, productVM: ProductViewmodel) {
    NavHost(navController = navController, startDestination = "homepage") {
        composable("homepage") { HomepageScreen(
            productVM = productVM,
            modifier = modifier
        ) }
        composable("statistics") { StatisticsScreen(
            padding = modifier,
            productVM = productVM
        ) }
    }
}

@Composable
fun NavigationBar(
    navController: NavController,
    scanLauncher: ActivityResultLauncher<Intent>,
) {
    val items = listOf("homepage", "statistics", "scanner", "logout")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.PieChart, Icons.Filled.QrCodeScanner, Icons.Filled.Logout)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.PieChart, Icons.Outlined.QrCodeScanner, Icons.Outlined.Logout)

    val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route
    val context = LocalContext.current

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (currentRoute == item) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item.replaceFirstChar { it.uppercase() }) },
                selected = currentRoute == item,
                onClick = {
                    when (item) {
                        "scanner" -> {
                            // Launch the scanner activity
                            val intent = Intent(context, ScannerActivity::class.java)
                            scanLauncher.launch(intent)
                        }
                        "logout" -> {
                            // Handle logout logic here
                            SharedPrefManager.clearLoginState()
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        }
                        else -> {
                            if (currentRoute != item) {
                                navController.navigate(item) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}