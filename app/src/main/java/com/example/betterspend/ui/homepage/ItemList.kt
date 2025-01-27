package com.example.betterspend.ui.homepage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.betterspend.utils.SharedPrefManager
import com.example.betterspend.viewmodel.ProductViewmodel

@Composable
fun ItemList(
    modifier: Modifier,
    productVM: ProductViewmodel
) {
    val uiState = productVM.uiState.collectAsState()
    val context = LocalContext.current

    // Fetch logged user products
    productVM.fetchProducts(SharedPrefManager.getUserId().toString())

    Log.d("MIAO", uiState.value.productItems.products.toString())

    if (uiState.value.productItems.products.isNotEmpty()) {
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
    } else {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("You have not scanned any products yet")
        }
    }


    Log.d("MIAO", uiState.value.barcodeScanMessage)

    if (uiState.value.barcodeScanMessage.isNotEmpty()) {
        Toast.makeText(context, uiState.value.barcodeScanMessage, Toast.LENGTH_SHORT).show()
        uiState.value.barcodeScanMessage = ""
    }

}