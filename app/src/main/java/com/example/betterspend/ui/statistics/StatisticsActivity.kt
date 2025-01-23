package com.example.betterspend.ui.statistics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.betterspend.ui.common.NavigationBar
import com.example.betterspend.viewmodel.ProductViewmodel
import kotlin.getValue

class StatisticsActivity : ComponentActivity() {
    private val productVM : ProductViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                bottomBar = { NavigationBar(productVM) }
            ) { innerPadding ->
                StatisticsScreen(Modifier.padding(innerPadding), productVM)
            }
        }
    }
}

@Composable
fun StatisticsScreen(padding: Modifier, productVM: ProductViewmodel) {
    StatisticsHeader(modifier = padding
        .fillMaxWidth()
    )
    StatisticsBody()
}