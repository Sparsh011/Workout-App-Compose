package com.sparshchadha.workout_app.ui.screens.reminders

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.ReminderEntity
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkGray
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.ColorsUtil.unselectedBottomBarIconColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PIE_CHART_SIZE
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.RemindersViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemindersScreen(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    remindersViewModel: RemindersViewModel,
) {
    LaunchedEffect(
        key1 = true,
        block = {
            remindersViewModel.getRemindersByReminderType(ReminderTypes.FOOD.name)
            remindersViewModel.getRemindersByReminderType(ReminderTypes.EXERCISE.name)
        }
    )

    val foodReminders = remindersViewModel.foodReminders.collectAsStateWithLifecycle().value
    val workoutReminders = remindersViewModel.workoutReminders.collectAsStateWithLifecycle().value

    val pagerState = rememberPagerState {
        2
    }

    var showBottomSheetToAddReminder by remember {
        mutableStateOf(false)
    }

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val tabBarHeadings = listOf("Exercise", "Food")

    if (showBottomSheetToAddReminder) {
        BottomSheetToAddReminder(
            hideBottomSheet = {
                showBottomSheetToAddReminder = false
            }
        ) {
            remindersViewModel.addReminder(it)
        }
    }

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Reminders",
                onBackButtonPressed = {
                    navController.popBackStack(
                        route = BottomBarScreen.CalorieTracker.route,
                        inclusive = false
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheetToAddReminder = true
                },
                containerColor = primaryPurple,
                contentColor = White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        },
        modifier = Modifier.padding(bottom = globalPaddingValues.calculateBottomPadding())
    ) { localPaddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(scaffoldBackgroundColor)
                .padding(
                    top = localPaddingValues.calculateTopPadding(),
                    bottom = globalPaddingValues.calculateBottomPadding(),
                    start = MEDIUM_PADDING,
                    end = MEDIUM_PADDING,
                )
        ) {

            LaunchedEffect(key1 = selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }

            LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
                if (!pagerState.isScrollInProgress) {
                    selectedTabIndex = pagerState.currentPage
                }
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth(),
                divider = {
                    Divider(
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(horizontal = MEDIUM_PADDING)
                    )
                },
                indicator = {
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(it[selectedTabIndex])
                            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                            .padding(horizontal = MEDIUM_PADDING)
                    )
                },
                containerColor = scaffoldBackgroundColor
            ) {
                tabBarHeadings.forEachIndexed { index, heading ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(
                                text = heading,
                                color = primaryTextColor,
                                fontSize = 20.nonScaledSp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(1f)
                    )
                }
            }

            FoodAndExerciseReminderPager(
                pagerState = pagerState,
                foodReminders,
                workoutReminders = workoutReminders,
                globalPaddingValues = globalPaddingValues,
                localPaddingValues = localPaddingValues,
                deleteReminder = {
                    remindersViewModel.deleteReminder(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodAndExerciseReminderPager(
    pagerState: PagerState,
    foodReminders: List<ReminderEntity>?,
    workoutReminders: List<ReminderEntity>?,
    globalPaddingValues: PaddingValues,
    deleteReminder: (ReminderEntity) -> Unit,
    localPaddingValues: PaddingValues,
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                Reminders(
                    reminders = workoutReminders,
                    localPaddingValues = localPaddingValues,
                    globalPaddingValues = globalPaddingValues,
                    deleteReminder = deleteReminder
                )
            }

            1 -> {
                Reminders(
                    reminders = foodReminders,
                    localPaddingValues = localPaddingValues,
                    globalPaddingValues = globalPaddingValues,
                    deleteReminder = deleteReminder
                )
            }
        }
    }
}

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
        ShowNewDatePicker(
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
        ShowNewTimePicker(
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
        windowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = White
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
                    containerColor = primaryTextColor
                ),
                modifier = Modifier
                    .padding(LARGE_PADDING)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add Reminder",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = White
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNewDatePicker(
    hideDatePicker: () -> Unit,
    updateDay: (Int) -> Unit,
    updateMonth: (Int) -> Unit,
    updateYear: (Int) -> Unit,
) {
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MM yyyy")
                .format(pickedDate)
        }
    }


    val dateDialogState = rememberMaterialDialogState()
    dateDialogState.show()
    val context = LocalContext.current

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                hideDatePicker()
                Toast.makeText(
                    context,
                    "Clicked ok with picked date $pickedDate",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel") {
                hideDatePicker()
            }
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
            allowedDateValidator = {
                it.dayOfMonth >= LocalDate.now().dayOfMonth && it.monthValue >= LocalDate.now().monthValue
            }
        ) {
            updateDay(it.dayOfMonth)
            updateMonth(it.monthValue)
            updateYear(it.year)
            pickedDate = it
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNewTimePicker(
    hideTimePicker: () -> Unit,
    updateHour: (Int) -> Unit,
    updateMinutes: (Int) -> Unit,
) {
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }

    val timeDialogState = rememberMaterialDialogState()
    timeDialogState.show()
    val context = LocalContext.current

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                hideTimePicker()
                Toast.makeText(
                    context,
                    "Clicked ok with $pickedTime",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel") {
                hideTimePicker()
            }
        }
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = "Pick a time"
        ) {
            updateMinutes(it.minute)
            updateHour(it.hour)
            pickedTime = it
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
            Text(text = "Select Date", color = primaryDarkGray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Black,
            unfocusedTextColor = primaryDarkGray,
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
            Text(text = "Select Time", color = primaryDarkGray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Black,
            unfocusedTextColor = primaryDarkGray,
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
        if (selectedReminderType == ReminderTypes.EXERCISE.name) {
            Text(
                text = ReminderTypes.EXERCISE.name.lowercase().capitalize(),
                color = White,
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        updateSelectedReminderType(ReminderTypes.EXERCISE.name)
                    }
                    .padding(LARGE_PADDING)
                    .clip(RoundedCornerShape(MEDIUM_PADDING))
                    .background(primaryTextColor)
                    .padding(MEDIUM_PADDING),

                textAlign = TextAlign.Center
            )

            Text(
                text = ReminderTypes.FOOD.name.lowercase().capitalize(),
                color = Black,
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        updateSelectedReminderType(ReminderTypes.FOOD.name)
                    }
                    .background(White)
                    .padding(MEDIUM_PADDING),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = ReminderTypes.EXERCISE.name.lowercase().capitalize(),
                color = Black,
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        updateSelectedReminderType(ReminderTypes.EXERCISE.name)
                    }
                    .background(White)
                    .padding(MEDIUM_PADDING),
                textAlign = TextAlign.Center
            )

            Text(
                text = ReminderTypes.FOOD.name.lowercase().capitalize(),
                color = White,
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        updateSelectedReminderType(ReminderTypes.FOOD.name)
                    }
                    .padding(LARGE_PADDING)
                    .clip(RoundedCornerShape(MEDIUM_PADDING))
                    .background(primaryTextColor)
                    .padding(MEDIUM_PADDING),
                textAlign = TextAlign.Center
            )
        }
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
            Text(text = "Reminder Description", color = primaryDarkGray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Black,
            unfocusedTextColor = primaryDarkGray,
            disabledLabelColor = primaryDarkGray,
            focusedPlaceholderColor = primaryDarkGray,
            unfocusedPlaceholderColor = primaryDarkGray,
            disabledPlaceholderColor = primaryDarkGray,
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerHeadings(
    pagerState: PagerState,
    updatePagerState: (Int) -> Unit,
) {

    Row(
        horizontalArrangement = Arrangement.Center
    ) {

        SelectedPagerHeading(
            text = "Food",
            modifier = Modifier
                .width(
                    LocalConfiguration.current.screenWidthDp.dp / 2
                )
                .clickable(
                    indication = null,
                    onClick = {
                        if (pagerState.currentPage != 0) updatePagerState(0)
                    },
                    interactionSource = remember { MutableInteractionSource() }
                ),
            isSelected = pagerState.currentPage == 0,
            dividerWidth = LocalConfiguration.current.screenWidthDp.dp / 2
        )

        SelectedPagerHeading(
            text = "Exercise",
            modifier = Modifier
                .width(
                    LocalConfiguration.current.screenWidthDp.dp / 2
                )
                .clickable(
                    indication = null,
                    onClick = {
                        if (pagerState.currentPage != 1) updatePagerState(1)
                    },
                    interactionSource = remember { MutableInteractionSource() }
                ),
            isSelected = pagerState.currentPage == 1,
            dividerWidth = LocalConfiguration.current.screenWidthDp.dp / 2
        )
    }
}

@Composable
fun SelectedPagerHeading(
    text: String,
    modifier: Modifier,
    isSelected: Boolean,
    dividerWidth: Dp,
) {
    Column {
        if (isSelected) {
            Text(
                text = text,
                modifier = modifier,
                textAlign = TextAlign.Center,
                color = primaryTextColor,
                fontSize = 20.nonScaledSp,
                fontWeight = FontWeight.Bold
            )

            Divider(
                modifier = Modifier
                    .width(dividerWidth)
                    .padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING)
            )
        } else {
            Text(
                text = text,
                modifier = modifier,
                textAlign = TextAlign.Center,
                color = unselectedBottomBarIconColor,
                fontSize = 20.nonScaledSp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Reminders(
    reminders: List<ReminderEntity>?,
    globalPaddingValues: PaddingValues,
    localPaddingValues: PaddingValues,
    deleteReminder: (ReminderEntity) -> Unit,
) {

    if (reminders != null) {
        val currentDateTime = LocalDateTime.now()
        val verticalArrangement = if (reminders.isEmpty()) Arrangement.Center else Arrangement.Top


        val (pastReminders, upcomingReminders) = reminders.partition { reminder ->
            val reminderDateTime = LocalDateTime.of(
                reminder.year.toInt(),
                Month.valueOf(
                    HelperFunctions.getMonthFromIndex(reminder.month.toInt()).uppercase()
                ).value,
                reminder.date.toInt(),
                reminder.hours,
                reminder.minutes
            )
            reminderDateTime < currentDateTime
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = verticalArrangement
        ) {

            if (pastReminders.isNotEmpty()) {
                stickyHeader {
                    Text(
                        buildAnnotatedString {
                            append("Past")
                            withStyle(
                                style = SpanStyle(
                                    color = primaryTextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.nonScaledSp,
                                )
                            ) {
                                append(" Reminders")
                            }
                        },
                        fontSize = 20.nonScaledSp,
                        color = primaryTextColor,
                        modifier = Modifier.padding(MEDIUM_PADDING)
                    )
                }

                items(
                    items = pastReminders,
                    key = { it.id.toString() }
                ) { reminder ->
                    Reminder(
                        reminder = reminder,
                        deleteReminder = deleteReminder,
                        deleteIconColor = targetAchievedColor
                    )
                }
            }

            if (upcomingReminders.isNotEmpty()) {
                stickyHeader {
                    Text(
                        buildAnnotatedString {
                            append("Upcoming")
                            withStyle(
                                style = SpanStyle(
                                    color = primaryTextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.nonScaledSp,
                                )
                            ) {
                                append(" Reminders")
                            }
                        },
                        fontSize = 20.nonScaledSp,
                        color = primaryTextColor,
                        modifier = Modifier.padding(MEDIUM_PADDING)
                    )
                }

                items(
                    items = upcomingReminders,
                    key = { it.id.toString() }
                ) { reminder ->
                    Reminder(
                        reminder = reminder,
                        deleteReminder = deleteReminder,
                        deleteIconColor = noAchievementColor
                    )
                }
            }

            if (upcomingReminders.isEmpty() && pastReminders.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_svg),
                            contentDescription = null,
                            tint = noAchievementColor,
                            modifier = Modifier.size(PIE_CHART_SIZE)
                        )

                        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

                        Text(
                            text = "No Reminders",
                            fontSize = 18.nonScaledSp,
                            color = primaryTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Reminder(
    reminder: ReminderEntity,
    deleteReminder: (ReminderEntity) -> Unit,
    deleteIconColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING)
            .clip(RoundedCornerShape(MEDIUM_PADDING))
            .background(scaffoldBackgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
        ) {
            Text(
                text = reminder.reminderDescription.capitalize(),
                fontSize = 20.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                color = primaryTextColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(MEDIUM_PADDING)
            )

            val month by remember {
                mutableStateOf(
                    if (reminder.month.isNotBlank()) reminder.month.toInt().toString() else ""
                )
            }

            Text(
                buildAnnotatedString {
                    append(
                        "${reminder.date} ${
                            HelperFunctions.getMonthFromIndex(month.toInt()).substring(0..2)
                        } ${reminder.year}, "
                    )
                    withStyle(
                        style = SpanStyle(
                            color = primaryTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.nonScaledSp,
                        )
                    ) {
                        val hours = convertTo12HourFormat(reminder.hours)
                        val minutes =
                            if (reminder.minutes < 10) "0${reminder.minutes}" else reminder.minutes.toString()
                        append("${hours.first}: $minutes ${hours.second}")
                    }
                },
                fontSize = 14.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(vertical = SMALL_PADDING, horizontal = MEDIUM_PADDING),
                color = primaryTextColor
            )

            Text(
                buildAnnotatedString {
                    append("Reminder For ")
                    withStyle(
                        style = SpanStyle(
                            color = primaryTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.nonScaledSp,
                        )
                    ) {
                        append(reminder.reminderType.lowercase().capitalize())
                    }
                },
                fontSize = 14.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = primaryTextColor,
                modifier = Modifier.padding(
                    top = SMALL_PADDING,
                    bottom = MEDIUM_PADDING,
                    end = MEDIUM_PADDING,
                    start = MEDIUM_PADDING
                ),
            )
        }

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    deleteReminder(reminder)
                },
            tint = deleteIconColor
        )
    }
}

fun convertTo12HourFormat(hours24: Int): Pair<Int, String> {
    var hours12 = hours24 % 12
    if (hours12 == 0)
        hours12 = 12
    val period = if (hours24 < 12) "AM" else "PM"
    return Pair(hours12, period)
}
