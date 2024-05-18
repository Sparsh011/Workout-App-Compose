package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.ui.components.shared.CalendarRow
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodScreen(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodAndWaterViewModel: FoodAndWaterViewModel,
    profileViewModel: ProfileViewModel
) {
    val caloriesGoal = profileViewModel.caloriesGoal.collectAsState().value
    val caloriesConsumed = foodAndWaterViewModel.caloriesConsumed.collectAsState().value ?: "0"
    val selectedDateAndMonth =
        foodAndWaterViewModel.selectedDateAndMonthForFoodItems.collectAsState().value
    val waterGlassesGoal = profileViewModel.waterGlassesGoal.collectAsState().value

    val foodItemsConsumed = foodAndWaterViewModel.consumedFoodItems.collectAsState().value
    val nutrientsConsumed = foodAndWaterViewModel.nutrientsConsumed
    val waterGlassesConsumed = foodAndWaterViewModel.waterGlassesEntity.collectAsState().value

    var showDialogToUpdateCalories by remember {
        mutableStateOf(false)
    }

    var showAddDishesOptionsDialog by remember {
        mutableStateOf(false)
    }

    var progress by remember { mutableFloatStateOf(0F) }
    val progressAnimDuration = 1_000
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(progressAnimDuration), label = "",
    )
    LaunchedEffect(key1 = caloriesConsumed) {
        progress = (caloriesConsumed.toFloat() / caloriesGoal.toFloat())
    }

    LaunchedEffect(key1 = Unit) {

        foodAndWaterViewModel.getFoodItemsConsumedOn(
            selectedDateAndMonth.first.toString(),
            selectedDateAndMonth.second
        )

        foodAndWaterViewModel.getWaterGlassesConsumedOn(
            selectedDateAndMonth.first.toString(),
            selectedDateAndMonth.second
        )
    }

    if (showAddDishesOptionsDialog) {
        AddDishesOptionsDialog (
            hideDialog = {
                showAddDishesOptionsDialog = false
            },
            navigateToScreen = {
                navController.navigate(it)
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDishesOptionsDialog = true
                },
                containerColor = ColorsUtil.primaryBlue,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        },
        modifier = Modifier.padding(
            bottom = globalPaddingValues.calculateBottomPadding()
        )
    ) { localPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(scaffoldBackgroundColor)
                .padding(
                    top = localPaddingValues.calculateTopPadding()
                )
        ) {
            SearchBarToLaunchSearchScreen(
                navigateToSearchScreen = {
                    navController.navigate("SearchScreen/food")
                }
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                CalorieTrackerAndMacroNutrientsCardPager(
                    showDialogToUpdateCalories = showDialogToUpdateCalories,
                    toggleDialogToUpdateCalories = {
                        showDialogToUpdateCalories = it
                    },
                    caloriesGoal = caloriesGoal,
                    caloriesConsumed = caloriesConsumed,
                    waterGlassesGoal = waterGlassesGoal,
                    waterGlassesConsumedEntity = waterGlassesConsumed,
                    setWaterGlassesGoal = {
                        profileViewModel.setWaterGlassesGoal(it)
                    },
                    saveNewCaloriesGoal = {
                        profileViewModel.saveCaloriesGoal(it)
                    },
                    nutrientsConsumed = nutrientsConsumed,
                    updateWaterEntity = {
                        foodAndWaterViewModel.updateWaterGlassesEntity(it)
                    },
                    progressAnimation = progressAnimation
                )

                // Select day to show that day's calories and dishes consumed
                CalendarRow(
                    getResultsForDateAndMonth = {
                        foodAndWaterViewModel.getFoodItemsConsumedOn(it.first.toString(), it.second)
                        foodAndWaterViewModel.getWaterGlassesConsumedOn(
                            it.first.toString(),
                            it.second
                        )
                    },
                    selectedMonth = selectedDateAndMonth.second,
                    selectedDay = selectedDateAndMonth.first,
                    indicatorColor = noAchievementColor,
                )

                // Dishes consumed on header
                DishesConsumedOnAParticularDayHeader(
                    modifier = Modifier
                        .padding(MEDIUM_PADDING)
                        .fillMaxWidth()
                )

                FoodItemsConsumed(
                    foodItemsConsumed,
                    navController,
                    removeFoodItem = {
                        foodAndWaterViewModel.removeFoodItem(it)
                    }
                )
            }
        }
    }

}

@Composable
fun CurrentlySelectedCard(currentPage: Int, indicatorColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = SMALL_PADDING
            ),
        horizontalArrangement = Arrangement.Center
    ) {

        if (currentPage == 0) {
            Row {
                Canvas(
                    modifier = Modifier
                        .padding(SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = indicatorColor)
                }

                Canvas(
                    modifier = Modifier
                        .padding(SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = Color(0xFFCACACE))
                }
            }

        } else {
            Row {
                Canvas(
                    modifier = Modifier
                        .padding(SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = Color(0xFFCACACE))
                }

                Canvas(
                    modifier = Modifier
                        .padding(SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = indicatorColor)
                }
            }

        }
    }
}

@Composable
fun DishesConsumedOnAParticularDayHeader(modifier: Modifier) {
    Text(
        buildAnnotatedString {
            append("Dishes ")
            withStyle(
                style = SpanStyle(
                    color = primaryTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.nonScaledSp,
                )
            ) {
                append("Consumed")
            }
        },
        fontSize = 20.nonScaledSp,
        color = primaryTextColor,
        textAlign = TextAlign.Start,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis
    )
}

