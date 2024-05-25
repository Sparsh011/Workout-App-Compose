package com.sparshchadha.workout_app.features.gym.presentation.gym

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.DateRange
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.HelperFunctions
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityBarGraph(
    graphDateRange: DateRange,
    exercises: List<GymExercisesEntity>?
) {
    val graphValueTextColor = ColorsUtil.primaryTextColor.toArgb()
    val configuration = LocalConfiguration.current.screenHeightDp
    AndroidView(
        modifier = Modifier
            .height((configuration / 2).dp)
            .fillMaxWidth(),
        factory = { context ->
            val chart = BarChart(context)
            val dataSet = BarDataSet(getBarEntries(exercises, graphDateRange), "Number Of Sets").apply {
                color = ColorsUtil.primaryBlue.toArgb()
                valueTextColor = graphValueTextColor
            }
            chart.description.isEnabled = false
            val barDataSet = BarData(dataSet)
            chart.data = barDataSet

            chart.xAxis.valueFormatter = DateValueFormatter()
            chart.axisLeft.axisMinimum = 0f
            chart.axisLeft.axisMaximum = 10f
            chart.xAxis.setDrawGridLines(false)
            chart.xAxis.setDrawLabels(true)
            chart.setTouchEnabled(false)
            chart
        }
    )
}

private fun getBarEntries(exercises: List<GymExercisesEntity>?, dateRange: DateRange): List<BarEntry> {
    if (exercises == null) return emptyList()
    val currentDate = LocalDate.now()
    val startDate = when (dateRange) {
        DateRange.LAST_7_DAYS -> currentDate.minusDays(7)
        DateRange.LAST_30_DAYS -> currentDate.minusDays(30)
        DateRange.LAST_6_MONTHS -> currentDate.minusMonths(6)
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    val setsByDate = mutableMapOf<LocalDate, Int>()
    var date = startDate
    while (!date.isAfter(currentDate)) {
        setsByDate[date] = 0
        date = date.plusDays(1)
    }

    exercises.forEach { exercise ->
        try {
            val monthIndex = HelperFunctions.getMonthIndexFromName(exercise.month)
            val dateString =
                "%02d-%02d-%04d".format(exercise.date.toInt(), monthIndex, currentDate.year)
            val exerciseDate = LocalDate.parse(dateString, dateFormatter)
            if (exercise.isPerformed && (exerciseDate.isAfter(startDate) || exerciseDate.isEqual(
                    startDate
                ))
            ) {
                setsByDate[exerciseDate] =
                    setsByDate.getOrDefault(exerciseDate, 0) + exercise.setsPerformed
            }
        } catch (e: DateTimeParseException) {
            // Handle parse exception if needed
        }
    }

    val barEntries = setsByDate.map { (date, sets) ->
        BarEntry(date.toEpochDay().toFloat(), sets.toFloat())
    }

    return barEntries
}

class DateValueFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        return dateFormat.format(Date(value.toLong()))
    }
}