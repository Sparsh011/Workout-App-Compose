package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sparshchadha.workout_app.features.food.domain.entities.WaterEntity
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.HelperFunctions

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalorieTrackerAndMacroNutrientsCardPager(
    showDialogToUpdateCalories: Boolean,
    toggleDialogToUpdateCalories: (Boolean) -> Unit,
    caloriesGoal: String,
    caloriesConsumed: String,
    waterGlassesGoal: Int,
    waterGlassesConsumedEntity: WaterEntity?,
    setWaterGlassesGoal: (Int) -> Unit,
    saveNewCaloriesGoal: (String) -> Unit,
    nutrientsConsumed: Map<String, Double>,
    updateWaterEntity: (WaterEntity) -> Unit,
    progressAnimation: Float
) {
    val pagerState = rememberPagerState(
        pageCount = {
            2
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(Dimensions.SMALL_PADDING)
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    CaloriesConsumedCard(
                        showDialogToUpdateCalories = showDialogToUpdateCalories,
                        caloriesGoal = caloriesGoal,
                        hideUpdateCaloriesDialog = {
                            toggleDialogToUpdateCalories(false)
                        },
                        saveNewCaloriesGoal = {
                            saveNewCaloriesGoal(it)
                        },
                        showCaloriesGoalBottomSheet = {
                            toggleDialogToUpdateCalories(true)
                        },
                        caloriesConsumed = caloriesConsumed,
                        progressIndicatorColor = ColorsUtil.noAchievementColor,
                        waterGlassesGoal = waterGlassesGoal,
                        waterGlassesConsumed = waterGlassesConsumedEntity?.glassesConsumed ?: 0,
                        setWaterGlassesGoal = {
                            setWaterGlassesGoal(it)
                        },
                        setWaterGlassesConsumed = {
                            updateWaterEntity(
                                WaterEntity(
                                    glassesConsumed = it,
                                    date = waterGlassesConsumedEntity?.date
                                        ?: HelperFunctions.getCurrentDateAndMonth().first.toString(),
                                    month = waterGlassesConsumedEntity?.month
                                        ?: HelperFunctions.getCurrentDateAndMonth().second,
                                    year = waterGlassesConsumedEntity?.year ?: "2024",
                                    id = waterGlassesConsumedEntity?.id
                                )
                            )
                        },
                        progressAnimation = progressAnimation
                    )
                }

                1 -> {
                    MacroNutrientsConsumed(
                        nutrientsConsumed = nutrientsConsumed,
                        caloriesGoal = caloriesGoal
                    )
                }
            }
        }

        CurrentlySelectedCard(
            currentPage = pagerState.currentPage,
            indicatorColor = ColorsUtil.noAchievementColor
        )
    }
}