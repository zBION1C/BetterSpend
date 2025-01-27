package com.example.betterspend.ui.statistics

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.betterspend.utils.SharedPrefManager

@Composable
fun StatisticsHeader(modifier: Modifier) {
    // Use a Row to arrange the image and text horizontally
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Profile Image (Circular)
        AsyncImage(
            model = SharedPrefManager.getProfilePictureUri(),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop, // Crop to fit the circle
            modifier = Modifier
                .size(64.dp) // Adjust size as needed
                .clip(CircleShape) // Make it circular
                .background(Color.LightGray) // Optional background color
        )

        // Add some spacing between the image and text
        Spacer(modifier = Modifier.width(16.dp))

        // User Name
        Text(
            text = SharedPrefManager.getUserEmail().toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}