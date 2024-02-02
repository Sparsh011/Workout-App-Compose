package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryLightGray
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.HEADING_SIZE
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PIE_CHART_SIZE
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaloriesConsumedCard(
    shouldShowCaloriesBottomSheet: Boolean,
    sheetState: SheetState,
    caloriesGoal: String,
    hideCaloriesBottomSheet: () -> Unit,
    saveNewCaloriesGoal: (Float) -> Unit,
    showCaloriesGoalBottomSheet: () -> Unit,
    caloriesConsumed: String,
) {
    val configuration = LocalConfiguration.current;
    val caloriesConsumedCardWidth = configuration.screenWidthDp.dp
    val caloriesConsumedCardHeight = configuration.screenHeightDp.dp / 3

    Column(
        modifier = Modifier
            .width(caloriesConsumedCardWidth)
            .height(caloriesConsumedCardHeight)
            .padding(
                top = dimensionResource(id = R.dimen.large_padding),
                start = dimensionResource(id = R.dimen.large_padding),
                end = dimensionResource(id = R.dimen.large_padding),
                bottom = dimensionResource(id = R.dimen.medium_padding)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(primaryLightGray)
    ) {

        CardHeading(heading = "Daily Calories", headingSize = HEADING_SIZE)

        CaloriesConsumedAndLeft(
            caloriesConsumed = caloriesConsumed,
            showCaloriesGoalBottomSheet = showCaloriesGoalBottomSheet,
            caloriesGoal = caloriesGoal
        )

        CaloriesGoalText(showCaloriesGoalBottomSheet = showCaloriesGoalBottomSheet)
    }

    // Bottom sheet to update calories goal
    if (shouldShowCaloriesBottomSheet) {
        UpdateCaloriesBottomSheet(
            sheetState = sheetState,
            caloriesGoal = caloriesGoal,
            onSheetDismissed = {
                hideCaloriesBottomSheet()
            },
            saveNewCaloriesGoal = saveNewCaloriesGoal
        )
    }
}

@Composable
fun CaloriesGoalText(showCaloriesGoalBottomSheet: () -> Unit) {
    Text(
        text = "Calories Goal",
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING)
            .clickable {
                showCaloriesGoalBottomSheet()
            },
        color = primaryDarkTextColor,
        fontSize = 15.nonScaledSp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun CardHeading(
    heading: String,
    headingSize: Dp,
) {
    Text(
        buildAnnotatedString {
            append("Count Your ")
            withStyle(
                style = SpanStyle(
                    color = primaryDarkTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.nonScaledSp
                )
            ) {
                append(heading)
            }
        },
        fontSize = 22.nonScaledSp,
        color = primaryDarkTextColor,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING)
            .size(headingSize),
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CaloriesConsumedAndLeft(
    caloriesConsumed: String,
    showCaloriesGoalBottomSheet: () -> Unit,
    caloriesGoal: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        CaloriesLeftOrEatenColumn(
            calories = caloriesConsumed,
            description = "Eaten",
            modifier = Modifier
                .weight(1f)
                .padding(LARGE_PADDING),
        )

        CenterCaloriesGoalBox(
            caloriesGoal = caloriesGoal,
            modifier = Modifier
                .weight(1f)
                .clip(CircleShape)
                .align(CenterVertically)
                .size(Dimensions.PIE_CHART_SIZE)
                .clickable {
                    showCaloriesGoalBottomSheet()
                },
            caloriesConsumed = caloriesConsumed
        )

        CaloriesLeftOrEatenColumn(
            calories = (caloriesGoal.toInt() - caloriesConsumed.toInt()).toString(),
            description = "Left",
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(id = R.dimen.large_padding))
        )
    }
}

@Composable
fun CenterCaloriesGoalBox(modifier: Modifier, caloriesGoal: String, caloriesConsumed: String) {
    Box(
        modifier = modifier,
        contentAlignment = Center
    ) {

        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = primaryDarkTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.nonScaledSp,
                    )
                ) {
                    append(caloriesGoal)
                }
                append("\nkcal")
            },
            fontSize = 13.nonScaledSp,
            color = primaryDarkTextColor,
            textAlign = TextAlign.Center
        )

        val progress = caloriesConsumed.toFloat() / caloriesGoal.toFloat()

        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(PIE_CHART_SIZE),
            strokeWidth = MEDIUM_PADDING,
            trackColor = ColorsUtil.customDividerColor,
            color = ColorsUtil.targetAchievedColor,
            strokeCap  = StrokeCap.Round,
        )
    }
}


@Composable
fun CaloriesLeftOrEatenColumn(calories: String, description: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = description,
            modifier = Modifier.align(CenterHorizontally),
            fontWeight = FontWeight.Bold,
            fontSize = 20.nonScaledSp,
            color = Color.Black
        )

        if (calories.toInt() <= 0) {
            Text(
                text = "0 kcal",
                modifier = Modifier.align(CenterHorizontally),
                fontSize = 16.nonScaledSp,
                color = primaryDarkTextColor,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "$calories kcal",
                modifier = Modifier.align(CenterHorizontally),
                fontSize = 16.nonScaledSp,
                color = primaryDarkTextColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCaloriesBottomSheet(
    sheetState: SheetState,
    caloriesGoal: String,
    onSheetDismissed: () -> Unit,
    saveNewCaloriesGoal: (Float) -> Unit,
) {
    var newCaloriesGoal by remember {
        mutableFloatStateOf(caloriesGoal.toFloat())
    }

    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = Color.White,
        onDismissRequest = {
            onSheetDismissed()
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        BoxWithConstraints(
            Modifier
                .navigationBarsPadding()
                .padding(bottom = 10.dp)
        ) {
            Column {
                UpdateCaloriesGoalSlider(
                    calorieDescription = "Current Goal : ${caloriesGoal.toInt()}",
                    textModifier = Modifier.padding(10.dp),
                    sliderModifier = Modifier.padding(10.dp),
                    newCaloriesGoal = newCaloriesGoal,
                    updateNewCaloriesGoalInSlider = {
                        newCaloriesGoal = it
                    },
                    saveNewCaloriesGoal = saveNewCaloriesGoal,
                    hideUpdateCaloriesBottomSheet = onSheetDismissed
                )
            }
        }
    }
}

@Composable
fun UpdateCaloriesGoalSlider(
    calorieDescription: String,
    textModifier: Modifier,
    sliderModifier: Modifier,
    newCaloriesGoal: Float,
    updateNewCaloriesGoalInSlider: (Float) -> Unit,
    saveNewCaloriesGoal: (Float) -> Unit,
    hideUpdateCaloriesBottomSheet: () -> Unit,
) {
    Text(
        text = calorieDescription,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = textModifier
    )

    Text(
        text = "New Goal : ${newCaloriesGoal.toInt()}",
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = textModifier,
        overflow = TextOverflow.Ellipsis
    )

    Slider(
        value = newCaloriesGoal,
        onValueChange = {
            updateNewCaloriesGoalInSlider(it)
        },
        valueRange = 1000F..5000F,
        modifier = sliderModifier,
        colors = SliderDefaults.colors(
            thumbColor = primaryDarkTextColor,
            activeTrackColor = primaryDarkTextColor,
            inactiveTrackColor = primaryLightGray
        )
    )

    Button(
        onClick = {
            saveNewCaloriesGoal(newCaloriesGoal)
            hideUpdateCaloriesBottomSheet()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryDarkTextColor
        ),
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Update Calories Goal", color = Color.White)
    }

    Spacer(modifier = Modifier.height(10.dp))
}