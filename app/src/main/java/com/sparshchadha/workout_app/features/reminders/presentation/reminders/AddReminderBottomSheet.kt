package com.sparshchadha.workout_app.features.reminders.presentation.reminders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sparshchadha.workout_app.features.reminders.domain.entity.ReminderEntity
import com.sparshchadha.workout_app.ui.components.shared.NewDatePicker
import com.sparshchadha.workout_app.ui.components.shared.NewTimePicker
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.cardBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.BOTTOM_SHEET_BOTTOM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions.noRippleClickable

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetToAddReminder(
    hideBottomSheet: () -> Unit,
    addReminder: (ReminderEntity) -> Unit,
) {
    var selectedDate by remember {
        mutableStateOf("")
    }

    var selectedMonth by remember {
        mutableStateOf("")
    }

    var selectedYear by remember {
        mutableStateOf("")
    }

    var selectedHour by remember {
        mutableIntStateOf(12)
    }

    var selectedMinutes by remember {
        mutableIntStateOf(0)
    }

    var selectedReminderType by remember {
        mutableStateOf(ReminderTypes.EXERCISE.name)
    }

    var reminderDescription by remember {
        mutableStateOf("")
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    if (showDatePicker) {
        NewDatePicker(
            hideDatePicker = {
                showDatePicker = false
            },
            updateDay = {
                selectedDate = it.toString()
            },
            updateMonth = {
                selectedMonth = it.toString()
            },
            updateYear = {
                selectedYear = it.toString()
            }
        )
    }

    if (showTimePicker) {
        NewTimePicker(
            hideTimePicker = {
                showTimePicker = false
            },
            updateHour = {
                selectedHour = it
            },
            updateMinutes = {
                selectedMinutes = it
            },
        )
    }

    ModalBottomSheet(
        onDismissRequest = {
            hideBottomSheet()
        },
        sheetState = rememberModalBottomSheetState(),
        containerColor = cardBackgroundColor
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            SelectReminderType(
                selectedReminderType = selectedReminderType,
                updateSelectedReminderType = {
                    selectedReminderType = it
                }
            )

            ReminderDetailsTextField(
                reminderTitle = reminderDescription,
                onReminderTitleChange = {
                    reminderDescription = it
                }
            )

            SetDateAndTime(
                selectedDate = selectedDate,
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                selectedHour = selectedHour,
                selectedMinutes = selectedMinutes,
                showDatePicker = {
                    showDatePicker = true
                },
                showTimePicker = {
                    showTimePicker = true
                }
            )

            Button(
                onClick = {
                    addReminder(
                        ReminderEntity(
                            date = selectedDate,
                            month = selectedMonth,
                            year = selectedYear,
                            hours = selectedHour,
                            minutes = selectedMinutes,
                            reminderType = selectedReminderType,
                            reminderDescription = reminderDescription
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorsUtil.primaryPurple
                ),
                modifier = Modifier
                    .padding(
                        top = LARGE_PADDING,
                        bottom = BOTTOM_SHEET_BOTTOM_PADDING,
                        start = LARGE_PADDING,
                        end = LARGE_PADDING
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add Reminder",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun SetDateAndTime(
    selectedDate: String,
    selectedMonth: String,
    selectedYear: String,
    selectedHour: Int,
    selectedMinutes: Int,
    showDatePicker: () -> Unit,
    showTimePicker: () -> Unit,
) {

    OutlinedTextField(
        value = "$selectedDate / $selectedMonth / $selectedYear",
        onValueChange = {

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(LARGE_PADDING)
            .clickable {
                showDatePicker()
            },
        label = {
            Text(text = "Select Date", color = ColorsUtil.primaryDarkGray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = ColorsUtil.primaryDarkGray,
            disabledTextColor = primaryTextColor,
            disabledBorderColor = primaryTextColor
        ),
        enabled = false
    )

    OutlinedTextField(
        value = "$selectedHour: $selectedMinutes",
        onValueChange = {
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(LARGE_PADDING)
            .clickable {
                showTimePicker()
            },
        label = {
            Text(text = "Select Time", color = ColorsUtil.primaryDarkGray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = ColorsUtil.primaryDarkGray,
            disabledTextColor = primaryTextColor,
            disabledBorderColor = primaryTextColor
        ),
        enabled = false
    )
}


@Composable
fun SelectReminderType(
    selectedReminderType: String,
    updateSelectedReminderType: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ReminderTypes.EXERCISE.name.lowercase().capitalize(),
            color = (if(selectedReminderType == ReminderTypes.EXERCISE.name) Color.White else primaryTextColor),
            fontSize = 16.nonScaledSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .noRippleClickable {
                    updateSelectedReminderType(ReminderTypes.EXERCISE.name)
                }
                .padding(LARGE_PADDING)
                .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                .background(if(selectedReminderType == ReminderTypes.EXERCISE.name) ColorsUtil.primaryPurple else cardBackgroundColor)
                .padding(Dimensions.MEDIUM_PADDING),

            textAlign = TextAlign.Center
        )

        Text(
            text = ReminderTypes.FOOD.name.lowercase().capitalize(),
            color = (if(selectedReminderType == ReminderTypes.FOOD.name) Color.White else primaryTextColor),
            fontSize = 16.nonScaledSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .noRippleClickable {
                    updateSelectedReminderType(ReminderTypes.FOOD.name)
                }
                .padding(LARGE_PADDING)
                .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                .background(if(selectedReminderType == ReminderTypes.FOOD.name) ColorsUtil.primaryPurple else cardBackgroundColor)
                .padding(Dimensions.MEDIUM_PADDING),
            textAlign = TextAlign.Center
        )

        Text(
            text = ReminderTypes.WATER.name.lowercase().capitalize(),
            color = (if(selectedReminderType == ReminderTypes.WATER.name) Color.White else primaryTextColor),
            fontSize = 16.nonScaledSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .noRippleClickable {
                    updateSelectedReminderType(ReminderTypes.WATER.name)
                }
                .padding(LARGE_PADDING)
                .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                .background(if(selectedReminderType == ReminderTypes.WATER.name) ColorsUtil.primaryPurple else cardBackgroundColor)
                .padding(Dimensions.MEDIUM_PADDING),
            textAlign = TextAlign.Center
        )

    }
}

    @Composable
    fun ReminderDetailsTextField(
        reminderTitle: String,
        onReminderTitleChange: (String) -> Unit,
    ) {
        OutlinedTextField(
            value = reminderTitle,
            onValueChange = {
                onReminderTitleChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(LARGE_PADDING),
            placeholder = {

            },
            label = {
                Text(text = "Reminder Description", color = primaryTextColor)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = primaryTextColor,
                unfocusedTextColor = primaryTextColor,
                disabledLabelColor = primaryTextColor,
                focusedPlaceholderColor = primaryTextColor,
                unfocusedPlaceholderColor = primaryTextColor,
                disabledPlaceholderColor = primaryTextColor,
            )
        )
    }