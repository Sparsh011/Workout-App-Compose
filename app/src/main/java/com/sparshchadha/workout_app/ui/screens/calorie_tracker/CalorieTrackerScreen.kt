package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.WaterEntity
import com.sparshchadha.workout_app.ui.components.shared.CalendarRow
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.shared.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.FoodAndWaterViewModel
import com.sparshchadha.workout_app.viewmodel.ProfileViewModel

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodAndWaterViewModel: FoodAndWaterViewModel,
    profileViewModel: ProfileViewModel
) {
    val caloriesGoal = profileViewModel.caloriesGoal.collectAsState().value
    val caloriesConsumed = foodAndWaterViewModel.caloriesConsumed.value ?: "0"
    val selectedDateAndMonth =
        foodAndWaterViewModel.selectedDateAndMonthForFoodItems.collectAsState().value
    val waterGlassesGoal = profileViewModel.waterGlassesGoal.collectAsState().value

    val currentDate = HelperFunctions.getCurrentDateAndMonth().first
    val currentMonth = HelperFunctions.getCurrentDateAndMonth().second


    LaunchedEffect(key1 = Unit) {
        if (selectedDateAndMonth?.first != currentDate && selectedDateAndMonth?.second != currentMonth) {
            foodAndWaterViewModel.getFoodItemsConsumedOn()
            foodAndWaterViewModel.updateSelectedDay(
                selectedDateAndMonth?.first ?: 1,
                selectedDateAndMonth?.second ?: "Jan"
            )
            foodAndWaterViewModel.getWaterGlassesConsumedOn(
                date = (selectedDateAndMonth?.first
                    ?: HelperFunctions.getCurrentDateAndMonth().first).toString(),
                month = selectedDateAndMonth?.second
                    ?: HelperFunctions.getCurrentDateAndMonth().second,
                year = "2024"
            )
        }
    }

    val foodItemsConsumed = foodAndWaterViewModel.consumedFoodItems.collectAsState().value
    val nutrientsConsumed = foodAndWaterViewModel.nutrientsConsumed
    val selectedDayPair = foodAndWaterViewModel.selectedDayPosition
    val waterGlassesConsumed = foodAndWaterViewModel.waterGlassesEntity.collectAsState().value

    var showDialogToUpdateCalories by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

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

            // Show Today's calories and nutrients -
            val pagerState = rememberPagerState(
                pageCount = {
                    2
                }
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(SMALL_PADDING)
                ) {
                    HorizontalPager(state = pagerState) { page ->
                        when (page) {
                            0 -> {
                                CaloriesConsumedCard(
                                    showDialogToUpdateCalories = showDialogToUpdateCalories,
                                    caloriesGoal = caloriesGoal,
                                    hideUpdateCaloriesDialog = {
                                        showDialogToUpdateCalories = false
                                    },
                                    saveNewCaloriesGoal = {
                                        profileViewModel.saveCaloriesGoal(it)
                                    },
                                    showCaloriesGoalBottomSheet = {
                                        showDialogToUpdateCalories = true
                                    },
                                    caloriesConsumed = caloriesConsumed,
                                    progressIndicatorColor = noAchievementColor,
                                    waterGlassesGoal = waterGlassesGoal,
                                    waterGlassesConsumed = waterGlassesConsumed?.glassesConsumed
                                        ?: 0,
                                    setWaterGlassesGoal = {
                                        profileViewModel.setWaterGlassesGoal(it)
                                    },
                                    setWaterGlassesConsumed = {
                                        foodAndWaterViewModel.updateWaterGlassesEntity(
                                            WaterEntity(
                                                glassesConsumed = it,
                                                date = waterGlassesConsumed?.date
                                                    ?: HelperFunctions.getCurrentDateAndMonth().first.toString(),
                                                month = waterGlassesConsumed?.month
                                                    ?: HelperFunctions.getCurrentDateAndMonth().second,
                                                year = waterGlassesConsumed?.year ?: "2024",
                                                id = waterGlassesConsumed?.id
                                            )
                                        )
                                    },
                                    glassesConsumed = waterGlassesConsumed?.glassesConsumed ?: 0
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
                        indicatorColor = noAchievementColor
                    )
                }

                // Select day to show that day's calories and dishes consumed
                CalendarRow(
                    getResultsForDateAndMonth = {
                        foodAndWaterViewModel.getFoodItemsConsumedOn(it.first.toString(), it.second)
                        foodAndWaterViewModel.getWaterGlassesConsumedOn(
                            it.first.toString(),
                            it.second,
                            "2024"
                        )
                    },
                    selectedMonth = selectedDayPair.value.first,
                    selectedDay = selectedDayPair.value.second,
                    indicatorColor = noAchievementColor,
                    updateSelectedDayPair = {
                        foodAndWaterViewModel.updateSelectedDay(it.first, it.second)
                    }
                )

                // Dishes consumed on header
                DishesConsumedOnAParticularDayHeader(
                    modifier = Modifier
                        .padding(MEDIUM_PADDING)
                        .fillMaxWidth()
                )

                if (foodItemsConsumed.isNullOrEmpty()) {
                    NoWorkoutPerformedOrFoodConsumed(
                        text = "No Food Items Consumed",
                        textSize = 20.nonScaledSp,
                        iconSize = Dimensions.PIE_CHART_SIZE
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .padding(SMALL_PADDING)
                            .clip(RoundedCornerShape(MEDIUM_PADDING))
                            .background(bottomBarColor)
                    ) {
                        for (index in foodItemsConsumed.indices) {
                            ConsumedFoodItem(
                                consumedFoodItem = foodItemsConsumed[index],
                                showFoodItemDetails = {
                                    navController.navigate(route = "FoodItemDetails/${foodItemsConsumed[index].id}")
                                },
                                removeFoodItem = {
                                    foodAndWaterViewModel.removeFoodItem(foodItem = it)
                                },
                                showDivider = false
                            )
                        }
                    }
                }
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
fun FoodItemText(
    modifier: Modifier = Modifier,
    title: String = "Pasta",
    macroNutrient: String = "",
    quantityOfMacroNutrient: String = "",
) {
    if (macroNutrient.isBlank()) {
        Text(
            text = title,
            modifier = modifier,
            textAlign = TextAlign.Center,
            fontSize = 24.nonScaledSp,
            fontWeight = FontWeight.Bold,
            color = primaryTextColor,
            overflow = TextOverflow.Ellipsis
        )
    } else {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = macroNutrient,
                textAlign = TextAlign.Start,
                fontSize = 18.nonScaledSp,
                modifier = Modifier
                    .weight(2f)
                    .padding(MEDIUM_PADDING),
                fontWeight = FontWeight.Bold,
                color = primaryTextColor,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = quantityOfMacroNutrient,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING),
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Normal,
                color = primaryTextColor,
                overflow = TextOverflow.Ellipsis
            )
        }

        CustomDivider()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConsumedFoodItem(
    consumedFoodItem: FoodItemEntity,
    showFoodItemDetails: () -> Unit,
    removeFoodItem: (FoodItemEntity) -> Unit,
    showDivider: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SMALL_PADDING)
            .clickable {
                showFoodItemDetails()
            }
            .background(bottomBarColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SMALL_PADDING)
                .background(bottomBarColor)
        ) {
            consumedFoodItem.foodItemDetails?.name?.let { itemName ->
                Text(
                    text = "${itemName.capitalize()}, ${consumedFoodItem.servings} servings",
                    color = primaryTextColor,
                    modifier = Modifier
                        .padding(SMALL_PADDING)
                        .weight(3.75f),
                    fontSize = 16.nonScaledSp,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "${(consumedFoodItem.foodItemDetails?.calories ?: 0).toInt() * (consumedFoodItem.servings)} kcal",
                color = ColorsUtil.targetAchievedColor,
                modifier = Modifier
                    .padding(SMALL_PADDING)
                    .weight(1.25f),
                fontSize = 16.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )

            if (showDivider) {
                CustomDivider()
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

