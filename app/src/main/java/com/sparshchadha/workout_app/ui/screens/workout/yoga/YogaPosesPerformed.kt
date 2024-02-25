package com.sparshchadha.workout_app.ui.screens.workout.yoga

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.ui.components.CalendarRow
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel


private const val TAG = "YogaPosesPerformedToday"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GetYogaPosesPerformedOnParticularDay(
    yogaPosesPerformedToday: List<YogaEntity>?,
    uiEventState: WorkoutViewModel.UIEvent?,
    globalPaddingValues: PaddingValues,
    onBackButtonPressed: () -> Unit,
    getYogaPosesPerformedOn: (Pair<Int, String>) -> Unit,
    selectedDay: Int,
    selectedMonth: String,
) {
    if (uiEventState != null) {
        HandleUIEventState(
            event = uiEventState,
            yogaPosesPerformedToday = yogaPosesPerformedToday,
            globalPaddingValues = globalPaddingValues,
            onBackButtonPressed = onBackButtonPressed,
            getYogaPosesPerformedOn = getYogaPosesPerformedOn,
            selectedDay = selectedDay,
            selectedMonth = selectedMonth
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
) {
    when (event) {
        is WorkoutViewModel.UIEvent.ShowLoader -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.yoga_animation))
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
                selectedMonth = selectedMonth
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
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_gym_or_yoga_performed))
                    val progress by animateLottieCompositionAsState(composition)

                    NoWorkoutPerformedOrFoodConsumed(
                        text = "No Yoga Poses Performed",
                        composition = composition,
                        progress = progress,
                        localPaddingValues = localPaddingValues,
                        animationModifier = Modifier.size(Dimensions.LOTTIE_ANIMATION_SIZE_LARGE)
                    )
                }
            } else {
                items(yogaPosesPerformedToday) { yogaEntity ->
                    YogaEntityItem(yogaEntity = yogaEntity)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaEntityItem(yogaEntity: YogaEntity) {
    var shouldShowPoseDetailsBottomSheet by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            shouldShowPoseDetailsBottomSheet = true
        },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryLightGray
        )
    ) {
        Column(
            modifier = Modifier
                .background(ColorsUtil.primaryLightGray)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            yogaEntity.yogaPoseDetails?.let { pose ->
                Text(
                    buildAnnotatedString {
                        append("${pose.english_name} ")
                        withStyle(
                            style = SpanStyle(
                                color = ColorsUtil.primaryTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                        ) {
                            append("(${pose.sanskrit_name})")
                        }
                    },
                    color = ColorsUtil.primaryTextColor,
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    buildAnnotatedString {
                        append("Difficulty Level : ")
                        withStyle(
                            style = SpanStyle(
                                color = ColorsUtil.primaryTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        ) {
                            append(pose.difficulty_level)
                        }
                    },
                    color = ColorsUtil.primaryTextColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        append("Sets Performed : ")
                        withStyle(
                            style = SpanStyle(
                                color = ColorsUtil.primaryTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        ) {
                            append("${yogaEntity.setsPerformed}")
                        }
                    },
                    color = ColorsUtil.primaryTextColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (shouldShowPoseDetailsBottomSheet) {
        yogaEntity.yogaPoseDetails?.let {
            ShowYogaPoseDetailsInModalBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                toggleBottomSheetWithDetails = { showOrHideBottomSheet ->
                    shouldShowPoseDetailsBottomSheet = showOrHideBottomSheet
                },
                pose = it
            )
        }
    }
}