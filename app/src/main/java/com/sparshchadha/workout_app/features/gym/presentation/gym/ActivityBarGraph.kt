package com.sparshchadha.workout_app.features.gym.presentation.gym

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.Shape
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.DateRange
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.HelperFunctions
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Composable
fun ActivityBarGraph(
    graphDateRange: DateRange,
    exercises: List<GymExercisesEntity>?,
) {

    var showGraph by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        delay(1000L)
        showGraph = true
    }

    if (showGraph) {
        val modelProducer = remember { CartesianChartModelProducer.build() }

        LaunchedEffect(graphDateRange) {
            val dataSet = getBarEntries(exercises, graphDateRange)
            modelProducer.tryRunTransaction {
                columnSeries {
                    series(y = dataSet)
                }
            }
        }

        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            ColorsUtil.primaryBlue,
                            8f.dp,
                            Shape.rounded(40),
                        ),
                    ),
                ),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
            ),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.MEDIUM_PADDING),
        )
    } else {
        Column (
            modifier = Modifier.padding(Dimensions.LARGE_PADDING + Dimensions.LARGE_PADDING)
        ){
            ShowLoadingScreen()
        }
    }
}

private fun getBarEntries(
    exercises: List<GymExercisesEntity>?,
    dateRange: DateRange
): Collection<Number> {
    if (exercises == null) return emptyList()
    val currentDate = LocalDate.now()
    val startDate: LocalDate
    val lineClusterThreshold: Int // Used to club 'lineClusterThreshold' number of days on X axis to prevent spillage of graph

    when (dateRange) {
        DateRange.LAST_7_DAYS -> {
            startDate = currentDate.minusDays(6)
            lineClusterThreshold = 1
        }

        DateRange.LAST_30_DAYS -> {
            startDate = currentDate.minusDays(29)
            lineClusterThreshold = 6
        }

        DateRange.LAST_6_MONTHS -> {
            startDate = currentDate.minusMonths(5)
            lineClusterThreshold = 30
        }
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
        sets
    }

    return if (lineClusterThreshold == 1) barEntries
    else getBarEntriesWithCluster(
        lineClusterThreshold = lineClusterThreshold,
        barEntries = barEntries
    )
}

fun getBarEntriesWithCluster(lineClusterThreshold: Int, barEntries: List<Int>): Collection<Number> {
    val newEntries = mutableListOf<Number>()
    var i = barEntries.size - 1
    while (i >= 0) {
        var currentThreshold = lineClusterThreshold
        var sum = 0
        while (currentThreshold > 0 && i >= 0) {
            sum += barEntries[i--]
            currentThreshold--
        }
        newEntries.add(sum)
    }
    return newEntries.reversed()
}