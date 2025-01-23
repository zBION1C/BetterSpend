package com.example.betterspend.ui.homepage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.betterspend.ui.common.NavigationBar
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.ProductViewmodel


class HomepageActivity : ComponentActivity() {
    private val productVM : ProductViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fetch logged user products
        productVM.fetchProducts(SharedPrefManager.getUserId().toString())

        setContent {
            Scaffold(
                bottomBar = { NavigationBar(productVM) }
            ) { innerPadding ->
                HomepageScreen(Modifier.padding(innerPadding), productVM)
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomepageScreen(Modifier, ProductViewmodel())
}