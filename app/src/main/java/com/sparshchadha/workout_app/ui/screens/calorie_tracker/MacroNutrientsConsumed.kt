package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.times
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryLightGray
import com.sparshchadha.workout_app.util.ColorsUtil.unselectedBottomBarIconColor
import com.sparshchadha.workout_app.util.Constants
import com.sparshchadha.workout_app.util.Constants.CARBOHYDRATES_TOTAL_G
import com.sparshchadha.workout_app.util.Constants.FAT_TOTAL_G
import com.sparshchadha.workout_app.util.Constants.PROTEIN_G
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.HEADING_SIZE
import com.sparshchadha.workout_app.util.Dimensions.MACRONUTRIENT_TEXT_HEIGHT
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

private const val TAG = "MacroNutrientsConsumed"

@Composable
fun MacroNutrientsConsumed(nutrientsConsumed: Map<String, Double>) {
    val configuration = LocalConfiguration.current
    val macroNutrientsCardWidth = configuration.screenWidthDp.dp
    val macroNutrientsCardHeight = configuration.screenHeightDp.dp / 3

    Column(
        modifier = Modifier
            .width(macroNutrientsCardWidth)
            .height(macroNutrientsCardHeight)
            .padding(
                top = dimensionResource(id = R.dimen.large_padding),
                start = dimensionResource(id = R.dimen.large_padding),
                end = dimensionResource(id = R.dimen.large_padding),
                bottom = dimensionResource(id = R.dimen.medium_padding)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(primaryLightGray)
    ) {

        CardHeading("Daily Nutrients", headingSize = HEADING_SIZE)

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MacroNutrientNameAndConsumedQuantity(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = dimensionResource(id = R.dimen.medium_padding),
                        start = dimensionResource(id = R.dimen.medium_padding),
                        bottom = dimensionResource(id = R.dimen.medium_padding)
                    ),
                nutrientsConsumed = nutrientsConsumed
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = dimensionResource(id = R.dimen.medium_padding),
                        bottom = dimensionResource(id = R.dimen.medium_padding)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G]?.let { total ->
                    val pieCharEntries = mutableListOf<PieChartEntry>()
                    nutrientsConsumed[PROTEIN_G]?.let { protein ->
                        Constants.COLOR_TO_NUTRIENT_MAP[PROTEIN_G]?.let {
                            PieChartEntry(
                                color = it,
                                percentage = (protein / total).toFloat()
                            )
                        }?.let {
                            pieCharEntries.add(
                                it
                            )
                        }
                    }

                    nutrientsConsumed[CARBOHYDRATES_TOTAL_G]?.let { carbs ->
                        Constants.COLOR_TO_NUTRIENT_MAP[CARBOHYDRATES_TOTAL_G]?.let {
                            PieChartEntry(
                                color = it,
                                percentage = (carbs / total).toFloat()
                            )
                        }?.let {
                            pieCharEntries.add(
                                it
                            )
                        }
                    }

                    nutrientsConsumed[FAT_TOTAL_G]?.let { fats ->
                        Constants.COLOR_TO_NUTRIENT_MAP[FAT_TOTAL_G]?.let {
                            PieChartEntry(
                                color = it,
                                percentage = (fats / total).toFloat()
                            )
                        }?.let {
                            pieCharEntries.add(
                                it
                            )
                        }
                    }
                    if (pieCharEntries.size == 0) {
                        pieCharEntries.add(
                            PieChartEntry(color = unselectedBottomBarIconColor, 1f)
                        )
                    }
                    PieChart(
                        entries = pieCharEntries,
                        size = dimensionResource(id = R.dimen.pie_chart_size)
                    )
                }
            }
        }
    }
}

@Composable
fun MacroNutrientNameAndConsumedQuantity(
    modifier: Modifier,
    nutrientsConsumed: Map<String, Double>,
) {
    Column(
        modifier = modifier
    ) {

        MacroNutrient(
            macroNutrientName = "Carbohydrates",
            macroNutrientQuantity = nutrientsConsumed[CARBOHYDRATES_TOTAL_G],
            indicatorColor = Constants.COLOR_TO_NUTRIENT_MAP[CARBOHYDRATES_TOTAL_G]
        )

        Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

        MacroNutrient(
            macroNutrientName = "Protein",
            nutrientsConsumed[PROTEIN_G],
            indicatorColor = Constants.COLOR_TO_NUTRIENT_MAP[PROTEIN_G]
        )

        Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

        MacroNutrient(
            macroNutrientName = "Fats",
            nutrientsConsumed[FAT_TOTAL_G],
            indicatorColor = Constants.COLOR_TO_NUTRIENT_MAP[FAT_TOTAL_G]
        )
    }
}

@Composable
fun MacroNutrient(
    macroNutrientName: String,
    macroNutrientQuantity: Double?,
    indicatorColor: Color?,
) {
    Row(
        verticalAlignment = CenterVertically
    ) {

        indicatorColor?.let {
            PieChartIndicator(
                color = it
            )
        }

        Spacer(modifier = Modifier.width(Dimensions.SMALL_PADDING))

        MacroNutrientAnnotatedString(
            macroNutrientName = macroNutrientName,
            macroNutrientQuantity = (macroNutrientQuantity ?: 0.0).toString() ,
            modifier = Modifier
                .align(CenterVertically)
                .height(MACRONUTRIENT_TEXT_HEIGHT)
        )
    }
}

data class PieChartEntry(val color: Color, val percentage: Float)

fun calculateStartAngles(entries: List<PieChartEntry>): List<Float> {
    var totalPercentage = 0f
    return entries.map { entry ->
        val startAngle = totalPercentage * 360
        totalPercentage += entry.percentage
        startAngle
    }
}

@Composable
fun PieChart(entries: List<PieChartEntry>, size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val startAngles = calculateStartAngles(entries)
        entries.forEachIndexed { index, entry ->
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

@Composable
fun MacroNutrientAnnotatedString(
    macroNutrientName: String,
    macroNutrientQuantity: String,
    modifier: Modifier
) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = primaryDarkTextColor,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("$macroNutrientName ")
            }
            append("$macroNutrientQuantity g")
        },
        color = primaryDarkTextColor,
        modifier = modifier,
        fontSize = 14.nonScaledSp,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun PieChartIndicator(color: Color) {
    Canvas(
        modifier = Modifier
            .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)
    ) {
        drawCircle(color = color)
    }
}