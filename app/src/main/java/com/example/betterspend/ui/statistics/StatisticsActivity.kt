package com.example.betterspend.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.ProductViewmodel

@Composable
fun StatisticsScreen(padding: Modifier, productVM: ProductViewmodel) {

    productVM.fetchStatistics(SharedPrefManager.getUserId().toString())

    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        StatisticsHeader(
            modifier = padding
                .fillMaxWidth()
        )
        StatisticsBody(
            modifier = padding.fillMaxWidth(),
            productVM = productVM
        )
    }
}