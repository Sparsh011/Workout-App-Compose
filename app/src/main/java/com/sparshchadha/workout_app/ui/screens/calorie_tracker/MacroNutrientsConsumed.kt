package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryLightGray
import com.sparshchadha.workout_app.util.Constants
import com.sparshchadha.workout_app.util.Constants.CARBOHYDRATES_TOTAL_G
import com.sparshchadha.workout_app.util.Constants.FAT_TOTAL_G
import com.sparshchadha.workout_app.util.Constants.PROTEIN_G
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

private const val TAG = "MacroNutrientsConsumed"

@Composable
fun PieChartIndicator(color: Color) {
    Canvas(
        modifier = Modifier
            .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)
    ) {
        drawCircle(color = color)
    }
}

@Composable
fun MacroNutrientsConsumed(nutrientsConsumed: Map<String, Double>) {
    val configuration = LocalConfiguration.current;
    val macroNutrientsCardWidth = configuration.screenWidthDp.dp
    val macroNutrientsCardHeight = configuration.screenHeightDp.dp / 3

    Column(
        modifier = Modifier
            .width(macroNutrientsCardWidth)
            .height(macroNutrientsCardHeight)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(primaryLightGray)
    ) {

        CardHeading("Daily Nutrients", 0.1 * macroNutrientsCardHeight)

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MacroNutrientNameAndConsumedQuantity(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                macroNutrientsCardHeight = macroNutrientsCardHeight,
                nutrientsConsumed = nutrientsConsumed
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp, bottom = 10.dp),
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

//                    pieCharEntries.add(
//                        PieChartEntry(primaryDarkTextColor, 0.007f)
//                    )

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

//                    pieCharEntries.add(
//                        PieChartEntry(primaryDarkTextColor, 0.007f)
//                    )

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

//                    pieCharEntries.add(
//                        PieChartEntry(primaryDarkTextColor, 0.007f)
//                    )

                    PieChart(
                        entries = pieCharEntries,
                        size = 100.dp
                    )
                }
            }
        }
    }
}

@Composable
fun MacroNutrientNameAndConsumedQuantity(
    modifier: Modifier,
    macroNutrientsCardHeight: Dp,
    nutrientsConsumed: Map<String, Double>,
) {
    Column(
        modifier = modifier
    ) {

        MacroNutrient(
            macroNutrientName = "Carbohydrates",
            nutrientsConsumed[CARBOHYDRATES_TOTAL_G].toString(),
            indicatorColor = Constants.COLOR_TO_NUTRIENT_MAP[CARBOHYDRATES_TOTAL_G]
        )

        Spacer(modifier = Modifier.height(10.dp))

        MacroNutrient(
            macroNutrientName = "Protein",
            nutrientsConsumed[PROTEIN_G].toString(),
            indicatorColor = Constants.COLOR_TO_NUTRIENT_MAP[PROTEIN_G]
        )

        Spacer(modifier = Modifier.height(10.dp))

        MacroNutrient(
            macroNutrientName = "Fats",
            nutrientsConsumed[FAT_TOTAL_G].toString(),
            indicatorColor = Constants.COLOR_TO_NUTRIENT_MAP[FAT_TOTAL_G]
        )
    }
}

@Composable
fun MacroNutrient(
    macroNutrientName: String,
    macroNutrientQuantity: String,
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

        Spacer(modifier = Modifier.width(5.dp))

        MacroNutrientAnnotatedString(
            macroNutrientName = macroNutrientName,
            macroNutrientQuantity = macroNutrientQuantity,
            modifier = Modifier
                .align(CenterVertically)
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
        textAlign = TextAlign.Center
    )
}