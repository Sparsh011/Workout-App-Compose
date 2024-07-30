package com.sparshchadha.workout_app.ui.components.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class PieChartEntry(val color: Color, val percentage: Float)

@Composable
fun PieChart(entries: List<PieChartEntry>, size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val startAngles = calculateStartAngles(entries)
        entries.forEachIndexed loop@{ index, entry ->
            drawArc(
                color = entry.color,
                startAngle = startAngles[index],
                sweepAngle = entry.percentage * 360f,
                useCenter = true,
                topLeft = Offset.Zero,
                size = this.size,
            )
        }
    }
}

private fun calculateStartAngles(entries: List<PieChartEntry>): List<Float> {
    var totalPercentage = 0f
    val newList = mutableListOf<Float>()

    for (entry in entries) {
        if (entry.percentage == 0f) {
            continue
        }
        val startAngle = totalPercentage * 360
        totalPercentage += entry.percentage
        newList.add(startAngle)
    }

    return newList
}