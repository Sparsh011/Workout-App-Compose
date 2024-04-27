package com.sparshchadha.workout_app.features.reminders.presentation.reminders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.features.reminders.presentation.viewmodels.RemindersViewModel
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

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
            remindersViewModel.getRemindersByReminderType(ReminderTypes.WATER.name)
        }
    )

    val foodReminders = remindersViewModel.foodReminders.collectAsStateWithLifecycle().value
    val waterReminders = remindersViewModel.waterReminders.collectAsStateWithLifecycle().value
    val workoutReminders = remindersViewModel.workoutReminders.collectAsStateWithLifecycle().value

    val tabBarHeadings = listOf("Exercise", "Food", "Water")

    val pagerState = rememberPagerState {
        tabBarHeadings.size
    }

    var showBottomSheetToAddReminder by remember {
        mutableStateOf(false)
    }

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

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
        floatingActionButton = {
            AddReminderFab {
                showBottomSheetToAddReminder = true
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
                )
        ) {

            Text(
                text = "Reminders",
                fontSize = 20.nonScaledSp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorsUtil.statusBarColor)
                    .padding(
                        start = MEDIUM_PADDING,
                        end = SMALL_PADDING,
                        top = SMALL_PADDING,
                        bottom = MEDIUM_PADDING
                    ),
                color = White
            )

            LaunchedEffect(key1 = selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }

            LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
                if (!pagerState.isScrollInProgress) {
                    selectedTabIndex = pagerState.currentPage
                }
            }

            RemindersTabRow(
                selectedTabIndex = selectedTabIndex,
                tabBarHeadings = tabBarHeadings,
                updateSelectedTabIndex = {
                    selectedTabIndex = it
                }
            )

            FoodAndExerciseReminderPager(
                pagerState = pagerState,
                foodReminders = foodReminders,
                workoutReminders = workoutReminders,
                globalPaddingValues = globalPaddingValues,
                localPaddingValues = localPaddingValues,
                waterReminders = waterReminders,
                deleteReminder = {
                    remindersViewModel.deleteReminder(it)
                }
            )
        }
    }
}
