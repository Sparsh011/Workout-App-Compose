package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.ui.components.shared.CalendarRow
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.shared.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.ProfileViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit,
    saveNewCaloriesGoal: (String) -> Unit,
    removeFoodItem: (FoodItemEntity) -> Unit,
    foodItemsViewModel: FoodItemsViewModel,
    profileViewModel: ProfileViewModel
) {
    val caloriesGoal = profileViewModel.caloriesGoal.collectAsState().value
    val caloriesConsumed = foodItemsViewModel.caloriesConsumed.value ?: "0"
    val selectedDateAndMonth = foodItemsViewModel.selectedDateAndMonthForFoodItems.collectAsState().value

    val currentDate = HelperFunctions.getCurrentDateAndMonth().first
    val currentMonth = HelperFunctions.getCurrentDateAndMonth().second


    LaunchedEffect(key1 = Unit) {
        if (selectedDateAndMonth?.first != currentDate && selectedDateAndMonth?.second != currentMonth) {
            foodItemsViewModel.getFoodItemsConsumedOn()
            foodItemsViewModel.updateSelectedDay(
                selectedDateAndMonth?.first ?: 1,
                selectedDateAndMonth?.second ?: "Jan"
            )
        }
    }

    val foodItemsConsumed = foodItemsViewModel.consumedFoodItems.collectAsState().value
    val nutrientsConsumed = foodItemsViewModel.nutrientsConsumed
    val selectedDayPair = foodItemsViewModel.selectedDayPosition

    var showDialogToUpdateCalories by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBackgroundColor)
            .padding(
                bottom = paddingValues.calculateBottomPadding()
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
                                saveNewCaloriesGoal = saveNewCaloriesGoal,
                                showCaloriesGoalBottomSheet = {
                                    showDialogToUpdateCalories = true
                                },
                                caloriesConsumed = caloriesConsumed,
                                progressIndicatorColor = noAchievementColor
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
                getResultsForDateAndMonth = getDishesConsumedOnSelectedDayAndMonth,
                selectedMonth = selectedDayPair.value.first,
                selectedDay = selectedDayPair.value.second,
                indicatorColor = noAchievementColor,
                updateSelectedDayPair = {
                    foodItemsViewModel.updateSelectedDay(it.first, it.second)
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
                            removeFoodItem = removeFoodItem,
                            showDivider = false
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getNextDate(currentDate: Int): Int {
    val currentLocalDate = LocalDate.ofYearDay(LocalDate.now().year, currentDate)
    val nextLocalDate = currentLocalDate.plusDays(1)
    return nextLocalDate.dayOfMonth
}

@RequiresApi(Build.VERSION_CODES.O)
fun getNextMonth(currentMonth: String): String {
    val currentLocalDate = LocalDate.ofYearDay(LocalDate.now().year, 1)
    val nextLocalDate = currentLocalDate.plusMonths(1)
    val formatter = DateTimeFormatter.ofPattern("MMMM")
    return if (nextLocalDate.monthValue == 1 && currentMonth.equals("December", ignoreCase = true)) "January"
    else nextLocalDate.format(formatter)
}

suspend fun PointerInputScope.detectSwipe(
    swipeState: MutableIntState = mutableIntStateOf(-1),
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {},
) = detectDragGestures(
    onDrag = { change, dragAmount ->
        change.consume()
        val (x, y) = dragAmount
        if (abs(x) > abs(y)) {
            when {
                x > 0 -> swipeState.intValue = 0
                x < 0 -> swipeState.intValue = 1
            }
        } else {
            when {
                y > 0 -> swipeState.intValue = 2
                y < 0 -> swipeState.intValue = 3
            }
        }
    },
    onDragEnd = {
        when (swipeState.intValue) {
            0 -> onSwipeRight()
            1 -> onSwipeLeft()
            2 -> onSwipeDown()
            3 -> onSwipeUp()
        }
    }
)

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
            Column(
                modifier = Modifier.weight(4f)
            ) {
                consumedFoodItem.foodItemDetails?.name?.let { itemName ->
                    Text(
//                        buildAnnotatedString {
//                            append("${consumedFoodItem.servings} x ")
//                            withStyle(
//                                style = SpanStyle(
//                                    color = primaryTextColor,
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 18.nonScaledSp,
//                                )
//                            ) {
//                                append(itemName.capitalize())
//                            }
//                        },
                        text = "${itemName.capitalize()}, ${consumedFoodItem.servings} servings",
                        color = primaryTextColor,
                        modifier = Modifier
                            .padding(SMALL_PADDING),
                        fontSize = 16.nonScaledSp,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )
                }


//                val hour12Format = consumedFoodItem.hour % 12
//                val amPm = if (consumedFoodItem.hour < 12) "AM" else "PM"
//                val formattedTime = LocalTime.of(hour12Format, consumedFoodItem.minutes)
//                    .format(DateTimeFormatter.ofPattern("hh: mm"))
//
//                Text(
//                    buildAnnotatedString {
//                        append("Consumed at ")
//                        withStyle(
//                            style = SpanStyle(
//                                fontWeight = FontWeight.Bold,
//                                color = primaryTextColor,
//                            )
//                        ) {
//                            append("$formattedTime $amPm")
//                        }
//                    },
//                    color = primaryTextColor,
//                    fontSize = 13.nonScaledSp
//                )
            }

//            Icon(
//                imageVector = Icons.Filled.Delete,
//                contentDescription = null,
//                modifier = Modifier
//                    .weight(1f)
//                    .align(CenterVertically)
//                    .clickable {
//                        removeFoodItem(consumedFoodItem)
//                    },
//                tint = noAchievementColor // red
//            )

            Text(
                text = "${(consumedFoodItem.foodItemDetails?.calories ?: 0).toInt() * (consumedFoodItem.servings)} kcal",
                color = targetAchievedColor,
                modifier = Modifier
                    .padding(SMALL_PADDING),
                fontSize = 16.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
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

