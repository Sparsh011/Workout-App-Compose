package com.sparshchadha.workout_app.features.gym.presentation.gym.goals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.gym.domain.entities.GoalEntity
import com.sparshchadha.workout_app.ui.components.shared.NewDatePicker
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetGoals(
    saveGoal: (GoalEntity) -> Unit
) {
    var showExtraFields by rememberSaveable {
        mutableStateOf(false)
    }
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gym))
    val animProgress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = 5
    )
    var goalDescription by rememberSaveable {
        mutableStateOf("")
    }
    var deadlineDay by rememberSaveable {
        mutableStateOf("")
    }
    var deadlineMonth by rememberSaveable {
        mutableStateOf("")
    }
    var deadlineYear by rememberSaveable {
        mutableStateOf("")
    }
    var showCalendarView by rememberSaveable {
        mutableStateOf(false)
    }
    var showRepsPicker by rememberSaveable {
        mutableStateOf(false)
    }
    var showPriorityDropdown by rememberSaveable {
        mutableStateOf(false)
    }
    var showWeightPicker by rememberSaveable {
        mutableStateOf(false)
    }
    var reps by rememberSaveable {
        mutableIntStateOf(0)
    }
    var weight by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }
    var selectedWeightUnit by rememberSaveable {
        mutableStateOf("kg")
    }
    var priority by rememberSaveable {
        mutableStateOf("Undefined")
    }
    var priorityColor by remember {
        mutableStateOf(Color.LightGray)
    }
    var showSuccessTickAnim by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimensions.MEDIUM_PADDING,
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {

        Column {
            LottieAnimation(
                composition = lottieComposition,
                progress = { animProgress },
                modifier = Modifier
                    .size(Dimensions.PIE_CHART_SIZE + Dimensions.MEDIUM_PADDING)
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

            OutlinedTextField(
                value = goalDescription,
                onValueChange = {
                    goalDescription = it
                },
                label = {
                    Text(
                        text = "Goal description",
                        color = ColorsUtil.primaryTextColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.MEDIUM_PADDING),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = ColorsUtil.scaffoldBackgroundColor,
                    focusedContainerColor = ColorsUtil.scaffoldBackgroundColor,
                    unfocusedBorderColor = ColorsUtil.primaryBlue,
                    focusedBorderColor = ColorsUtil.primaryBlue
                )
            )

            Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.MEDIUM_PADDING)
                    .clickable {
                        showExtraFields = !showExtraFields
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Set Additional Information",
                    fontSize = 18.nonScaledSp,
                    color = ColorsUtil.primaryTextColor
                )

                Icon(
                    imageVector = if (showExtraFields) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = ColorsUtil.noAchievementColor
                )
            }

            Spacer(modifier = Modifier.height(Dimensions.SMALL_PADDING))

            AnimatedVisibility(
                visible = showExtraFields,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                        .background(ColorsUtil.bottomBarColor)
                        .padding(Dimensions.MEDIUM_PADDING)
                ) {
                    SetDeadlineRow(
                        deadline = "$deadlineDay $deadlineMonth $deadlineYear",
                        modifier = Modifier
                            .clickable {
                                showCalendarView = true
                            }
                    )

                    Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

                    SetRepsRow(
                        reps = reps.toString(),
                        modifier = Modifier
                            .clickable {
                                showRepsPicker = true
                            }
                    )

                    Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

                    SetWeightRow(
                        weight = "$weight $selectedWeightUnit",
                        modifier = Modifier
                            .clickable {
                                showWeightPicker = true
                            }
                    )

                    Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

                    Box {
                        SetPriorityRow(
                            priority = priority,
                            modifier = Modifier
                                .clickable {
                                    showPriorityDropdown = true
                                },
                            priorityColor = priorityColor
                        )

                        PrioritiesMenu(
                            expanded = showPriorityDropdown,
                            dismiss = { showPriorityDropdown = false },
                            setPriority = { priority = it },
                            setPriorityColor = {
                                priorityColor = it
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            Button(
                onClick = {
                    saveGoal(
                        GoalEntity(
                            description = goalDescription,
                            deadlineDay = deadlineDay,
                            deadlineMonth = deadlineMonth,
                            deadlineYear = deadlineYear,
                            reps = reps,
                            targetWeight = weight,
                            priority = priority,
                            weightUnit = selectedWeightUnit
                        )
                    )
                    showSuccessTickAnim = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorsUtil.primaryBlue),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Save Goal",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = Dimensions.LARGE_PADDING)
                )
            }
        }
    }

    if (showCalendarView) {
        NewDatePicker(
            hideDatePicker = {
                showCalendarView = false
            },
            updateDay = {
                deadlineDay = it.toString()
            },
            updateMonth = {
                deadlineMonth = HelperFunctions.getMonthFromIndex(it)
            },
            updateYear = {
                deadlineYear = it.toString()
            }
        )
    }

    if (showRepsPicker) {
        NumberPicker(
            minValue = 1,
            maxValue = 50,
            setCurrentValue = {
                reps = it
            },
            hidePicker = { showRepsPicker = false },
            headingText = "Select Number Of Reps"
        )
    }

    if (showWeightPicker) {
        WeightPicker(
            weight = weight,
            setWeight = { weight = it },
            hidePicker = { showWeightPicker = false },
            headingText = "Select Weight",
            setWeightUnit = {
                selectedWeightUnit = it
            }
        )
    }
}

@Composable
private fun PrioritiesMenu(
    expanded: Boolean,
    dismiss: () -> Unit,
    setPriority: (String) -> Unit,
    setPriorityColor: (Color) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { dismiss() },
        modifier = Modifier
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(Dimensions.SMALL_PADDING)) {
                        drawCircle(color = ColorsUtil.noAchievementColor)
                    }

                    Spacer(modifier = Modifier.width(Dimensions.SMALL_PADDING))

                    Text(text = "High", color = ColorsUtil.primaryTextColor)
                }

            },
            onClick = {
                setPriority("High")
                setPriorityColor(ColorsUtil.noAchievementColor)
                dismiss()
            }
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(Dimensions.SMALL_PADDING)) {
                        drawCircle(color = ColorsUtil.partialTargetAchievedColor)
                    }

                    Spacer(modifier = Modifier.width(Dimensions.SMALL_PADDING))

                    Text(text = "Medium", color = ColorsUtil.primaryTextColor)
                }
            },
            onClick = {
                setPriority("Medium")
                setPriorityColor(ColorsUtil.partialTargetAchievedColor)
                dismiss()
            }
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(Dimensions.SMALL_PADDING)) {
                        drawCircle(color = ColorsUtil.targetAchievedColor)
                    }

                    Spacer(modifier = Modifier.width(Dimensions.SMALL_PADDING))

                    Text(text = "Low", color = ColorsUtil.primaryTextColor)

                }
            },
            onClick = {
                setPriority("Low")
                setPriorityColor(ColorsUtil.targetAchievedColor)
                dismiss()
            }
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(Dimensions.SMALL_PADDING)) {
                        drawCircle(color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.width(Dimensions.SMALL_PADDING))

                    Text(text = "Undefined", color = ColorsUtil.primaryTextColor)
                }
            },
            onClick = {
                setPriority("Undefined")
                setPriorityColor(Color.LightGray)
                dismiss()
            }
        )
    }
}

@Composable
private fun SetDeadlineRow(
    deadline: String,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set a Deadline",
            color = ColorsUtil.primaryTextColor,
            fontSize = 16.nonScaledSp,
            modifier = Modifier.weight(0.8f)
        )

        if (deadline.isNotBlank()) {
            Text(
                text = deadline,
                color = ColorsUtil.primaryTextColor,
                fontSize = 16.nonScaledSp,
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.weight(0.1f)
        )
    }
}

@Composable
private fun SetRepsRow(
    modifier: Modifier,
    reps: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set Number of Reps",
            color = ColorsUtil.primaryTextColor,
            fontSize = 16.nonScaledSp,
            modifier = Modifier.weight(0.8f)
        )

        if (reps.isNotBlank()) {
            Text(
                text = reps,
                color = ColorsUtil.primaryTextColor,
                fontSize = 16.nonScaledSp,
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.weight(0.1f)
        )
    }
}

@Composable
private fun SetWeightRow(
    modifier: Modifier,
    weight: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set Target Weight",
            color = ColorsUtil.primaryTextColor,
            fontSize = 16.nonScaledSp,
            modifier = Modifier.weight(0.8f)
        )

        if (weight.isNotBlank()) {
            Text(
                text = weight,
                color = ColorsUtil.primaryTextColor,
                fontSize = 16.nonScaledSp,
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.weight(0.1f)
        )
    }
}

@Composable
private fun SetPriorityRow(
    priority: String,
    modifier: Modifier,
    priorityColor: Color
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set Priority",
            color = ColorsUtil.primaryTextColor,
            fontSize = 16.nonScaledSp,
            modifier = Modifier.weight(0.7f)
        )

        Canvas(modifier = Modifier.size(Dimensions.SMALL_PADDING)) {
            drawCircle(color = priorityColor)
        }

        Spacer(modifier = Modifier.width(Dimensions.SMALL_PADDING))

        Text(
            text = priority,
            color = ColorsUtil.primaryTextColor,
            fontSize = 16.nonScaledSp,
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.weight(0.1f)
        )
    }
}