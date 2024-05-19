package com.sparshchadha.workout_app.features.gym.presentation.gym.goals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GymGoalsScreen(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues
) {
    val pagerState = rememberPagerState {
        2
    }
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    var goals = workoutViewModel.goalsSaved.collectAsStateWithLifecycle().value
    LaunchedEffect(key1 = Unit) {
        workoutViewModel.getAllSavedGoals()
    }

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Your Goals",
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
        },
        modifier = Modifier.padding(
            bottom = globalPaddingValues.calculateBottomPadding(),
        ),
        containerColor = ColorsUtil.scaffoldBackgroundColor
    ) { innerPaddingValues ->
        Column {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = innerPaddingValues.calculateTopPadding()),
                divider = {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = Dimensions.MEDIUM_PADDING),
                        thickness = 1.dp
                    )
                },
                indicator = {
                    SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(it[pagerState.currentPage])
                            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                            .padding(horizontal = Dimensions.MEDIUM_PADDING),
                        color = ColorsUtil.primaryBlue
                    )
                },
                containerColor = ColorsUtil.scaffoldBackgroundColor
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = {
                        selectedTabIndex = 0
                    },
                    text = {
                        Text(
                            text = "Create a Goal",
                            color = ColorsUtil.primaryTextColor,
                            fontSize = 16.nonScaledSp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(1f)
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = {
                        selectedTabIndex = 1
                    },
                    text = {
                        Text(
                            text = "Show Goals",
                            color = ColorsUtil.primaryTextColor,
                            fontSize = 16.nonScaledSp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(1f)
                )
            }

            LaunchedEffect(key1 = selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }

            LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
                if (!pagerState.isScrollInProgress) {
                    selectedTabIndex = pagerState.currentPage
                }
            }

            HorizontalPager(state = pagerState) {
                when (it) {
                    0 -> {
                        SetGoals(
                            saveGoal = { goal ->
                                workoutViewModel.saveGoal(goal)
                            }
                        )
                    }

                    1 -> {
                        ShowGoals(
                            goals = goals,
                            updateGoal = { goal ->
                                workoutViewModel.updateGoal(goal)
                            },
                            deleteGoal = { goal ->
                                workoutViewModel.deleteGoal(goal)
                            }
                        )
                    }
                }
            }
        }
    }
}
