package com.example.betterspend.ui.homepage

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.betterspend.viewmodel.ProductViewmodel

@Composable
fun ItemList(
    modifier: Modifier,
    productVM: ProductViewmodel
) {
    val uiState = productVM.uiState.collectAsState()

    // LazyColumn to display products
    LazyColumn(
        modifier = modifier
            .padding(10.dp)
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
}