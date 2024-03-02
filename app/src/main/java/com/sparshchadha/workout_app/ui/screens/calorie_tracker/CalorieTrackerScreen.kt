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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.ui.components.CalendarRow
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit,
    saveNewCaloriesGoal: (String) -> Unit,
    caloriesGoal: String,
    caloriesConsumed: String,
    selectedDateAndMonth: Pair<Int, String>?,
    removeFoodItem: (FoodItemEntity) -> Unit,
    foodItemsViewModel: FoodItemsViewModel,
) {

    LaunchedEffect(key1 = Unit) {
        foodItemsViewModel.updateSelectedDay(
            selectedDateAndMonth?.first ?: 1,
            selectedDateAndMonth?.second ?: "Jan"
        )
    }

    val foodItemsConsumed = foodItemsViewModel.savedFoodItems.collectAsStateWithLifecycle().value
    val nutrientsConsumed = foodItemsViewModel.nutrientsConsumed
    val selectedDayPair = foodItemsViewModel.selectedDayPosition

    var showDialogToUpdateCalories by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBackgroundColor)
            .padding(
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        stickyHeader {
            SearchBarToLaunchSearchScreen(
                navigateToSearchScreen = {
                    navController.navigate("SearchScreen/food")
                }
            )
        }

        // Show Today's calories and nutrients -
        item {
            val pagerState = rememberPagerState(
                pageCount = {
                    2
                }
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
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
                                saveNewCaloriesGoal = saveNewCaloriesGoal,
                                showCaloriesGoalBottomSheet = {
                                    showDialogToUpdateCalories = true
                                },
                                caloriesConsumed = caloriesConsumed,
                                progressIndicatorColor = primaryPurple
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
                    indicatorColor = primaryPurple
                )
            }
        }

        // Select day to show that day's calories and dishes consumed
        item {
            CalendarRow(
                getResultsForDateAndMonth = getDishesConsumedOnSelectedDayAndMonth,
                selectedMonth = selectedDayPair.value.first,
                selectedDay = selectedDayPair.value.second,
                indicatorColor =primaryPurple,
                updateSelectedDayPair = {
                    foodItemsViewModel.updateSelectedDay(it.first, it.second)
                }
            )
        }

        // Dishes consumed on header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically
            ) {
                DishesConsumedOnAParticularDayHeader(
                    modifier = Modifier.padding(MEDIUM_PADDING)
                )

                Spacer(modifier = Modifier.width(MEDIUM_PADDING))

                Icon(
                    painter = painterResource(id = R.drawable.reminders_svg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(LARGE_PADDING)
                        .clickable {
                            navController.navigate(UtilityScreen.RemindersScreen.route)
                        },
                    tint = primaryTextColor
                )
            }
        }

        // Dishes consumed on a particular day -
        if (foodItemsConsumed.isNullOrEmpty()) {
            item {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_food_item_added_animation))
                val progress by animateLottieCompositionAsState(composition)

                NoWorkoutPerformedOrFoodConsumed(
                    composition = composition,
                    progress = progress,
                    animationModifier = Modifier.size(
                        Dimensions.LOTTIE_ANIMATION_SIZE_LARGE
                    ),
                    textSize = 20.nonScaledSp
                )
            }
        } else {
            items(
                count = foodItemsConsumed.size,
                key = { index ->
                    foodItemsConsumed[index].id.toString()
                }
            ) { index ->
                ConsumedFoodItem(
                    consumedFoodItem = foodItemsConsumed[index],
                    showFoodItemDetails = {
                        navController.navigate(route = "FoodItemDetails/${foodItemsConsumed[index].id}")
                    },
                    removeFoodItem = removeFoodItem,
                    showDivider = index != foodItemsConsumed.size - 1
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
            .background(scaffoldBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SMALL_PADDING)
                .background(scaffoldBackgroundColor)
        ) {
            Column(
                modifier = Modifier.weight(4f)
            ) {
                consumedFoodItem.foodItemDetails?.name?.let { itemName ->
                    Text(
                        buildAnnotatedString {
                            append("${consumedFoodItem.servings} x ")
                            withStyle(
                                style = SpanStyle(
                                    color = primaryTextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.nonScaledSp,
                                )
                            ) {
                                append(itemName.capitalize())
                            }
                        },
                        color = primaryTextColor,
                        modifier = Modifier
                            .padding(SMALL_PADDING),
                        fontSize = 16.nonScaledSp,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )
                }


                Text(
                    buildAnnotatedString {
                        append("Consumed at ")
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                color = primaryTextColor,
                            )
                        ) {
                            append("${consumedFoodItem.hour}: ${consumedFoodItem.minutes}: ${consumedFoodItem.seconds}")
                        }
                    },
                    color = primaryTextColor,
                    fontSize = 13.nonScaledSp
                )
            }

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
                    .clickable {
                        removeFoodItem(consumedFoodItem)
                    },
                tint = ColorsUtil.noAchievementColor // red
            )
        }

        if (showDivider) {
            CustomDivider()
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

