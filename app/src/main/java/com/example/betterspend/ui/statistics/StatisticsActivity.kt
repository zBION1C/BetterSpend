package com.example.betterspend.ui.statistics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.betterspend.ui.common.NavigationBar
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.ProductViewmodel
import com.example.betterspend.viewmodel.StatisticsViewmodel
import kotlin.getValue

class StatisticsActivity : ComponentActivity() {
    private val statisticsVM : StatisticsViewmodel by viewModels()
    private val productVM : ProductViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statisticsVM.fetchStatistics(SharedPrefManager.getUserId().toString())

        setContent {
            Scaffold(
                bottomBar = { NavigationBar(productVM) }
            ) { innerPadding ->
                StatisticsScreen(Modifier.padding(innerPadding), statisticsVM)
            }
        }
    }
}

@Composable
fun StatisticsScreen(padding: Modifier, statisticsVM: StatisticsViewmodel) {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        StatisticsHeader(
            modifier = padding
                .fillMaxWidth()
        )
        StatisticsBody(
            modifier = padding.fillMaxWidth(),
            statisticsVM = statisticsVM
        )
    }
}