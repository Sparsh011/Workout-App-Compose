package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaloriesAndNutrientsConsumedToday(
    shouldShowCaloriesBottomSheet: Boolean,
    sheetState: SheetState,
    caloriesGoal: Float,
    hideCaloriesBottomSheet: () -> Unit,
    updateCaloriesGoal: (Float) -> Unit,
    showCaloriesGoalBottomSheet: () -> Unit,
) {
    val density = LocalDensity.current;
    val configuration = LocalConfiguration.current;
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    Column(
        modifier = Modifier
            .fillMaxWidth(screenWidth.toFloat())
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(ColorsUtil.primaryLightGray)
    ) {

        CardHeading()

        CaloriesConsumedAndLeftToday(
            caloriesGoal = caloriesGoal,
            showCaloriesGoalBottomSheet = showCaloriesGoalBottomSheet
        )

        Text(
            text = "Calories Goal",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .clickable {
                    showCaloriesGoalBottomSheet()
                },
            color = ColorsUtil.primaryDarkTextColor,
            fontSize = 15.nonScaledSp,
            textAlign = TextAlign.Center
        )
    }

    // Bottom sheet to update calories goal
    if (shouldShowCaloriesBottomSheet) {
        UpdateCaloriesBottomSheet(
            sheetState = sheetState,
            caloriesGoal = caloriesGoal,
            onSheetDismissed = {
                hideCaloriesBottomSheet()
            }
        ) {
            updateCaloriesGoal(it)
        }
    }
}

@Composable
fun CardHeading() {
    Text(
        buildAnnotatedString {
            append("Count Your ")
            withStyle(
                style = SpanStyle(
                    color = ColorsUtil.primaryDarkTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.nonScaledSp
                )
            ) {
                append("Daily Calories")
            }
        },
        fontSize = 24.nonScaledSp,
        color = ColorsUtil.primaryDarkTextColor,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

@Composable
fun CaloriesConsumedAndLeftToday(
    caloriesGoal: Float,
    showCaloriesGoalBottomSheet: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        CaloriesLeftOrEatenColumn(
            calories = caloriesGoal,
            description = "Eaten",
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        )

        CenterCaloriesGoalBox(
            calories = caloriesGoal,
            modifier = Modifier
                .weight(1f)
                .clip(CircleShape)
                .background(ColorsUtil.primaryGreenCardBackground)
                .align(CenterVertically)
                .size(100.dp)
                .clickable {
                    showCaloriesGoalBottomSheet()
                }
        )

        CaloriesLeftOrEatenColumn(
            calories = caloriesGoal, description = "Left",
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        )
    }
}

@Composable
fun CenterCaloriesGoalBox(modifier: Modifier, calories: Float) {
    Box(
        modifier = modifier,
        contentAlignment = Center
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = ColorsUtil.primaryDarkTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(24f, TextUnitType.Unspecified),
                    )
                ) {
                    append("${calories.toInt()}\n")
                }
                append("KCAL")
            },
            fontSize = TextUnit(18f, TextUnitType.Unspecified),
            color = ColorsUtil.primaryDarkTextColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CaloriesLeftOrEatenColumn(calories: Float, description: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = description,
            modifier = Modifier.align(CenterHorizontally),
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(24f, TextUnitType.Unspecified),
            color = Color.Black
        )
        Text(
            text = "${calories.toInt()} KCAL",
            modifier = Modifier.align(CenterHorizontally),
            fontSize = TextUnit(16f, TextUnitType.Unspecified),
            color = ColorsUtil.primaryDarkTextColor
        )
    }
}