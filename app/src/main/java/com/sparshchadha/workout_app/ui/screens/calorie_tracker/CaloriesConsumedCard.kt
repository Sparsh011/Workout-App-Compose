package com.sparshchadha.workout_app.ui.screens.calorie_tracker

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.cardBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryLightGray
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
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
    saveNewCaloriesGoal: (Int) -> Unit,
    showCaloriesGoalBottomSheet: () -> Unit,
    caloriesConsumed: String,
    progressIndicatorColor: Color,
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
            .background(cardBackgroundColor)
    ) {

        CardHeading(heading = "Daily Calories", headingSize = HEADING_SIZE)

        CaloriesConsumedAndLeft(
            caloriesConsumed = caloriesConsumed,
            showCaloriesGoalBottomSheet = showCaloriesGoalBottomSheet,
            caloriesGoal = caloriesGoal,
            progressIndicatorColor = targetAchievedColor
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
        color = primaryTextColor,
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
                    color = primaryTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.nonScaledSp
                )
            ) {
                append(heading)
            }
        },
        fontSize = 22.nonScaledSp,
        color = primaryTextColor,
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
    progressIndicatorColor: Color,
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
                .size(PIE_CHART_SIZE)
                .clickable {
                    showCaloriesGoalBottomSheet()
                },
            caloriesConsumed = caloriesConsumed,
            progressIndicatorColor = progressIndicatorColor
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
fun CenterCaloriesGoalBox(
    modifier: Modifier,
    caloriesGoal: String,
    caloriesConsumed: String,
    progressIndicatorColor: Color,
) {
    Box(
        modifier = modifier,
        contentAlignment = Center
    ) {

        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = primaryTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.nonScaledSp,
                    )
                ) {
                    append(caloriesGoal)
                }
                append("\nkcal")
            },
            fontSize = 13.nonScaledSp,
            color = primaryTextColor,
            textAlign = TextAlign.Center
        )

        val progress = caloriesConsumed.toFloat() / caloriesGoal.toFloat()

        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(PIE_CHART_SIZE),
            strokeWidth = MEDIUM_PADDING,
            trackColor = ColorsUtil.customDividerColor,
            color = progressIndicatorColor,
            strokeCap = StrokeCap.Round,
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
            color = primaryTextColor
        )

        if (calories.toInt() <= 0) {
            Text(
                text = "0 kcal",
                modifier = Modifier.align(CenterHorizontally),
                fontSize = 16.nonScaledSp,
                color = primaryTextColor,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "$calories kcal",
                modifier = Modifier.align(CenterHorizontally),
                fontSize = 16.nonScaledSp,
                color = primaryTextColor,
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
    saveNewCaloriesGoal: (Int) -> Unit,
) {

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
                    newCaloriesGoal = caloriesGoal,
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
    newCaloriesGoal: String,
    saveNewCaloriesGoal: (Int) -> Unit,
    hideUpdateCaloriesBottomSheet: () -> Unit,
) {
    var caloriesInput by remember {
        mutableStateOf(newCaloriesGoal)
    }

    Text(
        text = calorieDescription,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = textModifier
    )

    OutlinedTextField(
        value = caloriesInput,
        onValueChange = {
            caloriesInput = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(LARGE_PADDING),
        label = {
            Text(text = "Set Calories Goal", color = ColorsUtil.primaryDarkGray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = ColorsUtil.primaryDarkGray,
            disabledTextColor = primaryTextColor,
            disabledBorderColor = primaryTextColor
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Button(
        onClick = {
            saveNewCaloriesGoal(caloriesInput.toInt())
            hideUpdateCaloriesBottomSheet()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryTextColor
        ),
        modifier = Modifier
            .padding(LARGE_PADDING)
            .fillMaxWidth()
    ) {
        Text(text = "Set Calories Goal", color = Color.White)
    }

    Spacer(modifier = Modifier.height(MEDIUM_PADDING))
}