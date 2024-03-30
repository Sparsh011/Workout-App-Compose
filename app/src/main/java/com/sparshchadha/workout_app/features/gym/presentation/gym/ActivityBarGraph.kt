package com.sparshchadha.workout_app.features.gym.presentation.gym

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sparshchadha.workout_app.util.ColorsUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

@Composable
fun ActivityBarGraph() {
    val graphValueTextColor = ColorsUtil.primaryTextColor.toArgb()
    val configuration = LocalConfiguration.current.screenHeightDp
    AndroidView(
        modifier = Modifier
            .height((configuration / 2).dp)
            .fillMaxWidth(),
        factory = { context ->
            val chart = BarChart(context)

            // Generate dummy data
            val entries = mutableListOf<BarEntry>()
            val random = Random()
            for (i in 0 until 10) {
                entries.add(BarEntry(i.toFloat(), random.nextInt(10).toFloat()))
            }

            // Create a dataset with dummy data
            val dataSet = BarDataSet(entries, "Number Of Sets").apply {
                color = ColorsUtil.primaryPurple.toArgb()
                valueTextColor = graphValueTextColor
            }
            chart.description.isEnabled = false
            val barDataSet = BarData(dataSet)
            chart.data = barDataSet

            // Customize chart
            chart.xAxis.valueFormatter = DateValueFormatter()
            chart.axisLeft.axisMinimum = 0f
            chart.axisLeft.axisMaximum = 10f
            chart.axisRight.setDrawLabels(false)
            chart.axisRight.setDrawAxisLine(false)
            chart.setTouchEnabled(false)
            chart
        }
    )
}

class DateValueFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        return dateFormat.format(Date(value.toLong()))
    }
}