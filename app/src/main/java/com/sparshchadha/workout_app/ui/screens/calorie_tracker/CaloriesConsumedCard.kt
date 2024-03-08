package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.activity.components.LandingPageOutlinedTextField
import com.sparshchadha.workout_app.ui.screens.profile.AlertDialogToUpdate
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.carbohydratesColor
import com.sparshchadha.workout_app.util.ColorsUtil.cardBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions.HEADING_SIZE
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PIE_CHART_SIZE
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp


@Composable
fun CaloriesConsumedCard(
    showDialogToUpdateCalories: Boolean,
    caloriesGoal: String,
    hideUpdateCaloriesDialog: () -> Unit,
    saveNewCaloriesGoal: (String) -> Unit,
    showCaloriesGoalBottomSheet: () -> Unit,
    caloriesConsumed: String,
    progressIndicatorColor: Color,
    waterGlassesGoal: Int,
    waterGlassesConsumed: Int,
    setWaterGlassesGoal: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current;
    val caloriesConsumedCardWidth = configuration.screenWidthDp.dp
    val caloriesConsumedCardHeight = configuration.screenHeightDp.dp / 3

    Column(
        modifier = Modifier
            .width(caloriesConsumedCardWidth)
            .height(caloriesConsumedCardHeight)
            .padding(SMALL_PADDING)
            .clip(RoundedCornerShape(10.dp))
            .background(cardBackgroundColor)
    ) {

        CardHeading(heading = "Daily Calories", headingSize = HEADING_SIZE)

        CaloriesConsumedAndLeft(
            caloriesConsumed = caloriesConsumed,
            showCaloriesGoalBottomSheet = showCaloriesGoalBottomSheet,
            caloriesGoal = caloriesGoal,
            progressIndicatorColor = carbohydratesColor,
            waterGlassesConsumed = waterGlassesConsumed,
            waterGlassesGoal = waterGlassesGoal,
            setWaterGlassesGoal = setWaterGlassesGoal
        )

        CaloriesGoalText(showCaloriesGoalBottomSheet = showCaloriesGoalBottomSheet)
    }

    // Bottom sheet to update calories goal
    if (showDialogToUpdateCalories) {
        AlertDialogToUpdate(
            hideDialog = hideUpdateCaloriesDialog,
            value = caloriesGoal,
            onConfirmClick = {
                saveNewCaloriesGoal(it)
            },
            label = "Calories Goal (kcal)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
    waterGlassesConsumed: Int,
    waterGlassesGoal: Int,
    setWaterGlassesGoal: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            CaloriesLeftOrEatenColumn(
                calories = caloriesConsumed,
                description = "Eaten",
                modifier = Modifier
            )

            if (caloriesConsumed.isNotEmpty() && caloriesGoal.isNotEmpty()) {
                CaloriesLeftOrEatenColumn(
                    calories = (caloriesGoal.toInt() - caloriesConsumed.toInt()).toString(),
                    description = "Left",
                    modifier = Modifier
                )

                val percentage = String.format(
                    "%.1f",
                    (caloriesConsumed.toDouble() / caloriesGoal.toDouble()) * 100
                )

                Text(
                    text = "$percentage% consumed",
                    fontSize = 14.nonScaledSp,
                    color = targetAchievedColor,
                    textAlign = TextAlign.Start
                )
            }
        }

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

        Column(
            modifier = Modifier.weight(1f)
        ) {
            WaterTrackerColumn(
                waterGlassesConsumed = waterGlassesConsumed,
                waterGlassesGoal = waterGlassesGoal,
                setWaterGlassesGoal = setWaterGlassesGoal
            )
        }
    }
}

@Composable
fun WaterTrackerColumn(
    waterGlassesConsumed: Int,
    waterGlassesGoal: Int,
    setWaterGlassesGoal: (Int) -> Unit
) {
    WaterColumnItem(
        icon = R.drawable.water_glass,
        heading = "Water",
        text = waterGlassesConsumed.toString(),
        isVector = false,
        onClick = {

        }
    )

    Spacer(modifier = Modifier.height(SMALL_PADDING))

    var showDialogToSetWaterGoal by remember {
        mutableStateOf(false)
    }

    WaterColumnItem(
        icon = R.drawable.water_glass,
        heading = "Goal",
        text = waterGlassesGoal.toString(),
        isVector = false,
        onClick = {
            showDialogToSetWaterGoal = true
        }
    )

    if (showDialogToSetWaterGoal) {
        DialogToUpdateWaterGlasses(
            currentGoal = waterGlassesGoal,
            setWaterGlassesGoal = setWaterGlassesGoal,
            hideDialog = {
                showDialogToSetWaterGoal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogToUpdateWaterGlasses(
    currentGoal: Int,
    setWaterGlassesGoal: (Int) -> Unit,
    hideDialog: () -> Unit
) {
    var newGlassesGoal by remember {
        mutableStateOf(currentGoal.toString())
    }

    AlertDialog(
        onDismissRequest = {
            hideDialog()
        }
    ) {
        var showError by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(LARGE_PADDING))
                .background(ColorsUtil.bottomBarColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                buildAnnotatedString {
                    append("Current ")
                    withStyle(
                        style = SpanStyle(
                            color = primaryTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(currentGoal.toString())
                    }
                },
                color = primaryTextColor,
                modifier = Modifier
                    .padding(LARGE_PADDING)
            )

            LandingPageOutlinedTextField(
                label = "Set Water Goal",
                value = newGlassesGoal,
                onValueChange = {
                    showError = it.isEmpty()
                    if (it.isDigitsOnly()) newGlassesGoal = it
                },
                showErrorColor = showError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    if (newGlassesGoal.isNotBlank()) {
                        setWaterGlassesGoal(newGlassesGoal.toInt())
                        hideDialog()
                    }
                },
                modifier = Modifier
                    .padding(LARGE_PADDING)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorsUtil.primaryPurple
                )
            ) {
                Text(text = "Update", color = Color.White)
            }
        }
    }
}

@Composable
fun WaterColumnItem(
    icon: Any,
    isVector: Boolean,
    heading: String,
    text: String,
    showIcon: Boolean = true,
    textColor: Color = primaryTextColor,
    onClick: () -> Unit,
) {
    Text(
        text = heading,
        fontWeight = FontWeight.Bold,
        fontSize = 20.nonScaledSp,
        color = primaryTextColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(SMALL_PADDING))

    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.nonScaledSp,
            color = textColor,
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.width(SMALL_PADDING))

        if (showIcon) {
            if (isVector) {
                Icon(
                    imageVector = icon as ImageVector,
                    contentDescription = null,
                    modifier = Modifier.size(LARGE_PADDING - SMALL_PADDING),
                    tint = primaryBlue
                )
            } else {
                Icon(
                    painter = painterResource(id = (icon as Int)),
                    contentDescription = null,
                    modifier = Modifier.size(LARGE_PADDING),
                    tint = primaryBlue
                )
            }
        }
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

        if (caloriesConsumed.isNotEmpty() && caloriesGoal.isNotEmpty()) {
            val progress = caloriesConsumed.toFloat() / caloriesGoal.toFloat()

            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(PIE_CHART_SIZE),
                strokeWidth = MEDIUM_PADDING,
                trackColor = ColorsUtil.progressTrackColor,
                color = progressIndicatorColor,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}


@Composable
fun CaloriesLeftOrEatenColumn(calories: String, description: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = description,
            fontWeight = FontWeight.Bold,
            fontSize = 20.nonScaledSp,
            color = primaryTextColor,
            textAlign = TextAlign.Start
        )

        if (calories.toInt() <= 0) {
            Text(
                text = "0 kcal",
                fontSize = 14.nonScaledSp,
                color = primaryTextColor,
                textAlign = TextAlign.Start
            )
        } else {
            Text(
                text = "$calories kcal",
                fontSize = 14.nonScaledSp,
                color = primaryTextColor,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))
    }
}