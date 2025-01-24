package com.example.betterspend.ui.statistics

import android.graphics.Typeface
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.betterspend.viewmodel.StatisticsViewmodel
import kotlin.random.Random

fun getRandomColor(): Long {
    val red = Random.nextInt(0, 256)
    val green = Random.nextInt(0, 256)
    val blue = Random.nextInt(0, 256)
    return ((0xFF shl 24) or (red shl 16) or (green shl 8) or blue).toLong()
}

@Composable
fun StatisticsBody(modifier: Modifier, statisticsVM: StatisticsViewmodel) {
    val chartState = statisticsVM.uiState.collectAsState()
    val categories = chartState.value.statistics.categories

    Log.d("MIAO", "$categories")
    val context = LocalContext.current

    if (categories.isNotEmpty()) {
        val pieChartData = PieChartData(
            slices = categories.map { category ->
                PieChartData.Slice(
                    label = category.name,
                    value = category.frequency.toFloat(),
                    Color(getRandomColor())
                )
            },
            plotType = PlotType.Pie
        )

        val donutChartConfig = PieChartConfig(
            labelVisible = true,
            strokeWidth = 120f,
            labelColor = Color.Black,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
            isAnimationEnable = true,
            chartPadding = 25,
            labelFontSize = 42.sp,
            animationDuration = 1500
        )

        // Main layout for the chart and legend
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Legend
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                categories.forEachIndexed { index, category ->
                    val sliceColor = pieChartData.slices[index].color
                    LegendItem(color = sliceColor, label = category.name)
                }
            }

            // Pie Chart
            PieChart(
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp),
                pieChartData,
                donutChartConfig
            ) { slice ->
                Toast.makeText(context, slice.label, Toast.LENGTH_SHORT).show()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        Text("You have not scanned any products yet")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color box
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Label
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}


