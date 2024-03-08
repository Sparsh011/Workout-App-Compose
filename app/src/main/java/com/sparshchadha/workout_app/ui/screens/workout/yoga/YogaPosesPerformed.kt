package com.sparshchadha.workout_app.ui.screens.workout.yoga

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.CalendarRow
import com.sparshchadha.workout_app.ui.components.shared.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.cardBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter


private const val TAG = "YogaPosesPerformedToday"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GetYogaPosesPerformedOnParticularDay(
    navController: NavController,
    globalPaddingValues: PaddingValues,
    workoutViewModel: WorkoutViewModel
) {
    LaunchedEffect(
        key1 = true,
        block = {
            workoutViewModel.getYogaPosesPerformedOn()
        }
    )

    val yogaPosesPerformedToday = workoutViewModel.yogaPosesPerformed.value
    val uiEventState = workoutViewModel.yogaPosesPerformedOnUIEventState.collectAsStateWithLifecycle().value
    val selectedDayAndMonth = workoutViewModel.selectedDateAndMonthForYogaPoses.collectAsStateWithLifecycle().value

    if (uiEventState != null) {
        HandleUIEventState(
            event = uiEventState,
            yogaPosesPerformedToday = yogaPosesPerformedToday,
            globalPaddingValues = globalPaddingValues,
            selectedDay = selectedDayAndMonth?.first ?: 1,
            selectedMonth = selectedDayAndMonth?.second ?: "Jan",
            getYogaPosesPerformedOn = {
                workoutViewModel.getYogaPosesPerformedOn(
                    date = it.first.toString(),
                    month = it.second
                )
            },
            removePose = {
                workoutViewModel.removeYogaPoseFromDB(it)
            },
            onBackButtonPressed = {
                navController.popBackStack(
                    route = ScreenRoutes.WorkoutScreen.route,
                    inclusive = false
                )
            },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HandleUIEventState(
    event: WorkoutViewModel.UIEvent,
    yogaPosesPerformedToday: List<YogaEntity>?,
    globalPaddingValues: PaddingValues,
    onBackButtonPressed: () -> Unit,
    getYogaPosesPerformedOn: (Pair<Int, String>) -> Unit,
    selectedMonth: String,
    selectedDay: Int,
    removePose: (YogaEntity) -> Unit
) {
    when (event) {
        is WorkoutViewModel.UIEvent.ShowLoader -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
            val progress by animateLottieCompositionAsState(composition)

            ShowLoadingScreen(
                composition = composition, progress = progress
            )
        }

        is WorkoutViewModel.UIEvent.HideLoaderAndShowResponse -> {
            PopulatePerformedYogaPoses(
                yogaPosesPerformedToday = yogaPosesPerformedToday,
                globalPaddingValues = globalPaddingValues,
                onBackButtonPressed = onBackButtonPressed,
                getYogaPosesPerformedOn = getYogaPosesPerformedOn,
                selectedDay = selectedDay,
                selectedMonth = selectedMonth,
                removePose = removePose
            )
        }

        is WorkoutViewModel.UIEvent.ShowError -> {
            ErrorDuringFetch(errorMessage = event.errorMessage)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PopulatePerformedYogaPoses(
    yogaPosesPerformedToday: List<YogaEntity>?,
    globalPaddingValues: PaddingValues,
    onBackButtonPressed: () -> Unit,
    getYogaPosesPerformedOn: (Pair<Int, String>) -> Unit,
    selectedDay: Int,
    selectedMonth: String,
    removePose: (YogaEntity) -> Unit
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Today's Yoga Session",
                onBackButtonPressed = {
                    onBackButtonPressed()
                }
            )
        },
        containerColor = Color.White
    ) { localPaddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(scaffoldBackgroundColor)
                .padding(
                    top = localPaddingValues.calculateTopPadding(),
                    bottom = globalPaddingValues.calculateBottomPadding()
                )
        ) {
            stickyHeader {
                CalendarRow(
                    getResultsForDateAndMonth = getYogaPosesPerformedOn,
                    selectedMonth = selectedMonth,
                    selectedDay = selectedDay,
//                    indicatorColor = HelperFunctions.getAchievementColor(achieved = caloriesConsumed.toInt(), target = caloriesGoal.toInt())
                )
            }

            if (yogaPosesPerformedToday.isNullOrEmpty()) {
                item {
                    NoWorkoutPerformedOrFoodConsumed(
                        text = "No Yoga Poses Performed",
                        localPaddingValues = localPaddingValues,
                    )
                }
            } else {
                items(yogaPosesPerformedToday) { yogaEntity ->
                    YogaEntityItem(
                        yogaEntity = yogaEntity,
                        removePose = removePose
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaEntityItem(
    yogaEntity: YogaEntity,
    removePose: (YogaEntity) -> Unit
) {
    var shouldShowPoseDetailsBottomSheet by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING),
        onClick = {
            shouldShowPoseDetailsBottomSheet = true
        },
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMALL_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(9f)
                    .background(cardBackgroundColor)
                    .padding(MEDIUM_PADDING)
            ) {

                yogaEntity.yogaPoseDetails?.let { pose ->
                    Text(
                        buildAnnotatedString {
                            append(pose.english_name)
                            withStyle(
                                style = SpanStyle(
                                    color = ColorsUtil.primaryTextColor,
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
//                                append("(${pose.sanskrit_name})")
                            }
                        },
                        color = ColorsUtil.primaryTextColor,
                        fontSize = 20.nonScaledSp
                    )

                    Spacer(modifier = Modifier.height(SMALL_PADDING))

                    val hour12Format = yogaEntity.hour % 12
                    val amPm = if (yogaEntity.hour < 12) "AM" else "PM"
                    val formattedTime = LocalTime.of(hour12Format, yogaEntity.minutes)
                        .format(DateTimeFormatter.ofPattern("hh: mm"))

                    Text(
                        buildAnnotatedString {
                            append("Performed at ")
                            withStyle(
                                style = SpanStyle(
                                    color = ColorsUtil.primaryTextColor,
                                    )
                            ) {
                                append("$formattedTime $amPm")
                            }
                        },
                        color = ColorsUtil.primaryTextColor,
                        fontSize = 13.nonScaledSp
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = noAchievementColor,
                modifier = Modifier
                    .padding(SMALL_PADDING)
                    .clickable {
                        removePose(yogaEntity)
                    }
            )
        }
    }

    if (shouldShowPoseDetailsBottomSheet) {
        yogaEntity.yogaPoseDetails?.let {
            ShowYogaPoseDetailsInModalBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                toggleBottomSheetWithDetails = { showOrHideBottomSheet ->
                    shouldShowPoseDetailsBottomSheet = showOrHideBottomSheet
                },
                pose = it,
                saveYogaPose = {}
            )
        }
    }
}
