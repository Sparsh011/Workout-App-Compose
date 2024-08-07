package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.components.shared.PieChart
import com.sparshchadha.workout_app.ui.components.shared.PieChartEntry
import com.sparshchadha.workout_app.util.ColorsUtil.cardBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.progressTrackColor
import com.sparshchadha.workout_app.util.Constants.CARBOHYDRATES_TOTAL_G
import com.sparshchadha.workout_app.util.Constants.COLOR_TO_NUTRIENT_MAP
import com.sparshchadha.workout_app.util.Constants.FAT_TOTAL_G
import com.sparshchadha.workout_app.util.Constants.PROTEIN_G
import com.sparshchadha.workout_app.util.Constants.TOTAL_NUTRIENTS_G
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.HEADING_SIZE
import com.sparshchadha.workout_app.util.Dimensions.MACRONUTRIENT_TEXT_HEIGHT
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PIE_CHART_SIZE
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

private const val TAG = "MacroNutrientsConsumed"

@Composable
fun MacroNutrientsConsumed(nutrientsConsumed: Map<String, Double>, caloriesGoal: String) {
    val configuration = LocalConfiguration.current
    val macroNutrientsCardWidth = configuration.screenWidthDp.dp

    val carbsGoal: Float = ((caloriesGoal.toFloat() * 0.55) / 4).toFloat()
    val proteinGoal: Float = ((caloriesGoal.toFloat() * 0.20) / 4).toFloat()
    val fatsGoal: Float = ((caloriesGoal.toFloat() * 0.25) / 9).toFloat()

    Column(
        modifier = Modifier
            .width(macroNutrientsCardWidth)
            .fillMaxHeight(1f)
            .padding(SMALL_PADDING)
            .clip(RoundedCornerShape(10.dp))
            .background(cardBackgroundColor)
    ) {

        CardHeading("Daily Nutrients", headingSize = HEADING_SIZE)

        MacroNutrientsIndicator(nutrientsConsumed)

        MacroNutrientsProgressIndicator(
            nutrientsConsumed = nutrientsConsumed,
            carbsGoal = carbsGoal,
            proteinGoal = proteinGoal,
            fatsGoal = fatsGoal
        )
    }
}

@Composable
fun MacroNutrientsProgressIndicator(
    nutrientsConsumed: Map<String, Double>,
    carbsGoal: Float,
    proteinGoal: Float,
    fatsGoal: Float,
) {
    Row(
        modifier = Modifier.padding(horizontal = MEDIUM_PADDING)
    ) {

        nutrientsConsumed[CARBOHYDRATES_TOTAL_G]?.let { carbs ->
            MacroNutrientProgress(
                progress = (carbs / carbsGoal).toFloat(),
                color = COLOR_TO_NUTRIENT_MAP[CARBOHYDRATES_TOTAL_G]!!,
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING),
                goal = carbsGoal
            )
        }

        nutrientsConsumed[PROTEIN_G]?.let { protein ->
            MacroNutrientProgress(
                progress = (protein / proteinGoal).toFloat(),
                color = COLOR_TO_NUTRIENT_MAP[PROTEIN_G]!!,
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING),
                goal = proteinGoal
            )
        }

        nutrientsConsumed[FAT_TOTAL_G]?.let { fats ->
            MacroNutrientProgress(
                progress = (fats / fatsGoal).toFloat(),
                color = COLOR_TO_NUTRIENT_MAP[FAT_TOTAL_G]!!,
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING),
                goal = fatsGoal
            )
        }
    }
}

@Composable
fun MacroNutrientProgress(
    progress: Float,
    color: Color,
    modifier: Modifier,
    goal: Float,
) {
    Column(
        modifier = modifier
    ) {

        Text(
            text = String.format("%.1f", (goal)) + "g",
            fontSize = 10.nonScaledSp,
            color = primaryTextColor,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(SMALL_PADDING)
        )

        LinearProgressIndicator(
            progress = { progress },
            color = color,
            trackColor = progressTrackColor,
            strokeCap = StrokeCap.Round,
        )
    }
}

@Composable
fun MacroNutrientsIndicator(
    nutrientsConsumed: Map<String, Double>,
) {
    Row {
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

        MacroNutrientsPieChart(
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = dimensionResource(id = R.dimen.medium_padding),
                    bottom = dimensionResource(id = R.dimen.medium_padding)
                ),
            nutrientsConsumed = nutrientsConsumed
        )
    }
}

@Composable
fun MacroNutrientsPieChart(
    modifier: Modifier,
    nutrientsConsumed: Map<String, Double>,
) {
    var percentage by remember {
        mutableFloatStateOf(0f)
    }
    val pieCharEntries = remember {
        mutableStateListOf<PieChartEntry>()
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        nutrientsConsumed[TOTAL_NUTRIENTS_G]?.let { total ->
            nutrientsConsumed[PROTEIN_G]?.let { protein ->
                COLOR_TO_NUTRIENT_MAP[PROTEIN_G]?.let {
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
                COLOR_TO_NUTRIENT_MAP[CARBOHYDRATES_TOTAL_G]?.let {
                    PieChartEntry(
                        color = it,
                        percentage = (carbs / total).toFloat()
                    )
                }?.let {
                    percentage += it.percentage
                    pieCharEntries.add(
                        it
                    )
                }
            }

            nutrientsConsumed[FAT_TOTAL_G]?.let { fats ->
                COLOR_TO_NUTRIENT_MAP[FAT_TOTAL_G]?.let {
                    PieChartEntry(
                        color = it,
                        percentage = (fats / total).toFloat()
                    )
                }?.let {
                    percentage += it.percentage
                    pieCharEntries.add(
                        it
                    )
                }
            }
        }

        if (pieCharEntries.size == 0) {
            pieCharEntries.add(PieChartEntry(Color.DarkGray, 1f))
        }
        PieChart(
            entries = pieCharEntries.toList(),
            size = PIE_CHART_SIZE
        )
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
            indicatorColor = COLOR_TO_NUTRIENT_MAP[CARBOHYDRATES_TOTAL_G]
        )

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        MacroNutrient(
            macroNutrientName = "Protein",
            nutrientsConsumed[PROTEIN_G],
            indicatorColor = COLOR_TO_NUTRIENT_MAP[PROTEIN_G]
        )

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        MacroNutrient(
            macroNutrientName = "Fats",
            nutrientsConsumed[FAT_TOTAL_G],
            indicatorColor = COLOR_TO_NUTRIENT_MAP[FAT_TOTAL_G]
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

        Spacer(modifier = Modifier.width(SMALL_PADDING))

        MacroNutrientAnnotatedString(
            macroNutrientName = macroNutrientName,
            macroNutrientQuantity = String.format("%.1f", (macroNutrientQuantity ?: 0.0)),
            modifier = Modifier
                .align(CenterVertically)
                .height(MACRONUTRIENT_TEXT_HEIGHT)
        )
    }
}

@Composable
fun MacroNutrientAnnotatedString(
    macroNutrientName: String,
    macroNutrientQuantity: String,
    modifier: Modifier,
) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = primaryTextColor,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("$macroNutrientName ")
            }
            append("$macroNutrientQuantity g")
        },
        color = primaryTextColor,
        modifier = modifier
            .wrapContentSize(Center),
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