package com.sparshchadha.workout_app.ui.screens.reminders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.data.local.room_db.entities.ReminderEntity
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.ui_state.NoResultsFoundOrErrorDuringSearch
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkGray
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkTextColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.RemindersViewModel
import kotlinx.coroutines.launch

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

    val coroutineScope = rememberCoroutineScope()

    var showBottomSheetToAddReminder by remember {
        mutableStateOf(false)
    }

    if (showBottomSheetToAddReminder) {
        BottomSheetToAddReminder(
            hideBottomSheet = {
                showBottomSheetToAddReminder = false
            },
            addReminder = {
                remindersViewModel.addReminder(it)
            }
        )
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
//                    remindersViewModel.addReminder(
//                        ReminderEntity(
//                            "21",
//                            "January",
//                            10,
//                            30,
//                            reminderType = ReminderTypes.EXERCISE.name
//                        )
//                    )
                    showBottomSheetToAddReminder = true
                }
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
                .background(White)
                .fillMaxSize()
                .padding(
                    top = localPaddingValues.calculateTopPadding(),
                    bottom = globalPaddingValues.calculateBottomPadding(),
                    start = MEDIUM_PADDING,
                    end = MEDIUM_PADDING,
                )
        ) {

            PagerHeadings(
                pagerState = pagerState,
                updatePagerState = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(it)
                    }
                }
            )

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {
                        UpcomingReminders(
                            reminders = foodReminders,
                            localPaddingValues = localPaddingValues,
                            globalPaddingValues = globalPaddingValues
                        )
                    }

                    1 -> {
                        UpcomingReminders(
                            reminders = workoutReminders,
                            localPaddingValues = localPaddingValues,
                            globalPaddingValues = globalPaddingValues
                        )
                    }
                }
            }
        }
    }
}

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

            Button(
                onClick = {
                    addReminder(
                        ReminderEntity(
                            date = selectedDate,
                            month = selectedMonth,
                            hours = selectedHour,
                            minutes = selectedMinutes,
                            reminderType = selectedReminderType,
                            reminderDescription = reminderDescription
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryDarkTextColor
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
                text = ReminderTypes.EXERCISE.name,
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
                    .background(primaryDarkTextColor)
                    .padding(MEDIUM_PADDING),

                textAlign = TextAlign.Center
            )

            Text(
                text = ReminderTypes.FOOD.name,
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
                text = ReminderTypes.EXERCISE.name,
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
                text = ReminderTypes.FOOD.name,
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
                    .background(primaryDarkTextColor)
                    .padding(MEDIUM_PADDING),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ReminderDetailsTextField(
    reminderTitle: String,
    onReminderTitleChange: (String) -> Unit
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
            Text(text = "Add Reminder Description", color = primaryDarkGray)
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
        Text(
            text = text,
            modifier = modifier,
            textAlign = TextAlign.Center,
            color = primaryDarkTextColor,
            fontSize = 20.nonScaledSp,
            fontWeight = FontWeight.Bold
        )

        if (isSelected) {
            Divider(
                modifier = Modifier
                    .width(dividerWidth)
                    .padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING)
            )
        }
    }
}

@Composable
fun UpcomingReminders(
    reminders: List<ReminderEntity>?,
    globalPaddingValues: PaddingValues,
    localPaddingValues: PaddingValues,
) {
    if (reminders != null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (reminders.isNotEmpty()) {
                items(
                    items = reminders,
                    key = {
                        it.id.toString()
                    }
                ) { reminder ->
                    Text(text = "Time - ${reminder.hours} and ${reminder.minutes}, date - ${reminder.date}, month - ${reminder.month}")
                }
            } else {
                item {
                    NoResultsFoundOrErrorDuringSearch(
                        globalPaddingValues = globalPaddingValues,
                        localPaddingValues = localPaddingValues,
                        message = "No Reminders Added"
                    )
                }
            }
        }
    }
}
