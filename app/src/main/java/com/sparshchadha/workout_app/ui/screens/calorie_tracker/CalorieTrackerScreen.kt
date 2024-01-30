package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.entities.FoodItemEntity
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.HelperFunctions
import kotlinx.coroutines.launch

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    foodItemsConsumedToday: List<FoodItemEntity>?,
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit
) {
    var caloriesGoal by remember {
        mutableFloatStateOf(1000F)
    }

    var caloriesConsumedToday by remember {
        mutableFloatStateOf(0F)
    }

    var shouldShowCaloriesBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()

    var searchBarQuery by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            SearchBarToLaunchSearchScreen(
                searchBarQuery = searchBarQuery,
                context = context,
                navController = navController,
                focusManager = focusManager,
                updateSearchBarQuery = {
                    searchBarQuery = it
                }
            )
        }
    ) { localPaddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = paddingValues.calculateBottomPadding(), top = localPaddingValues.calculateTopPadding())
        ) {
            // Show Today's calories and nutrients -
            item {
                CaloriesAndNutrientsConsumedToday(
                    shouldShowCaloriesBottomSheet = shouldShowCaloriesBottomSheet,
                    sheetState = sheetState,
                    caloriesGoal,
                    hideCaloriesBottomSheet = {
                        shouldShowCaloriesBottomSheet = false
                    },
                    updateCaloriesGoal = {
                        caloriesGoal = it
                    },
                    showCaloriesGoalBottomSheet = {
                        shouldShowCaloriesBottomSheet = true
                    }
                )
            }

            item {
                SelectDay(
                    getDishesConsumedOnSelectedDayAndMonth = getDishesConsumedOnSelectedDayAndMonth
                )
            }

            // Today's consumed dishes -

            if (foodItemsConsumedToday.isNullOrEmpty()) {
                item {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_food_item_added_animation))
                    val progress by animateLottieCompositionAsState(composition)

                    NoWorkoutPerformedOrFoodConsumed(
                        text = "No Dishes Consumed Yet!",
                        composition = composition,
                        progress = progress,
                        animationModifier = Modifier.size(200.dp),
                        textSize = 20.sp
                    )
                }

            } else {
                item {
                    DishesConsumedTodayHeader()
                }

                items(foodItemsConsumedToday) { foodItem ->
                    Column {
                        foodItem.foodItemDetails?.name?.let { it1 -> Text(text = it1) }
                        Text(text = foodItem.date)
                        Text(text = foodItem.month)
                    }
                }
            }
        }
    }
}

@Composable
fun DishesConsumedTodayHeader() {
    Text(
        buildAnnotatedString {
            append("Dishes Consumed ")
            withStyle(
                style = SpanStyle(
                    color = ColorsUtil.primaryDarkTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(24f, TextUnitType.Unspecified),
                )
            ) {
                append("Today")
            }
        },
        fontSize = TextUnit(24f, TextUnitType.Unspecified),
        color = ColorsUtil.primaryDarkTextColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(15.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectDay(
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit
) {
    val last30Days = HelperFunctions.getLast30Days()
    last30Days.reverse()

    val next3Days = HelperFunctions.getNext3Days()

    val (currentDay, currentMonth) = HelperFunctions.getCurrentDateAndMonth()

    LazyRow {
        items(last30Days) {
            DayAndDate(date = it.first.toString(), month = it.second, modifier = Modifier.clickable {
                getDishesConsumedOnSelectedDayAndMonth(Pair(it.first, it.second))
            })
        }

        items(next3Days) {
            DayAndDate(date = it.first.toString(), month = it.second )
        }
    }

}

@Composable
fun DayAndDate(
    date: String,
    month: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = month,
            fontSize = TextUnit(20f, TextUnitType.Unspecified),
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = date,
            fontSize = TextUnit(18f, TextUnitType.Unspecified),
            color = ColorsUtil.primaryDarkTextColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCaloriesBottomSheet(
    sheetState: SheetState,
    caloriesGoal: Float,
    onSheetDismissed: () -> Unit,
    onCaloriesChanged: (Float) -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = Color.Black,
        onDismissRequest = {
            onSheetDismissed()
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        BoxWithConstraints(
            Modifier
                .navigationBarsPadding()
                .padding(bottom = 10.dp)
        ) {
            Column {
                CaloriesConsumedAndSliderComposable(
                    calorieDescription = "Calories Goal : ${caloriesGoal.toInt()}",
                    shouldEnableSlider = true,
                    calories = caloriesGoal,
                    textModifier = Modifier.padding(10.dp),
                    sliderModifier = Modifier.padding(10.dp)
                ) {
                    onCaloriesChanged(it)
                }
            }
        }
    }
}

@Composable
fun CaloriesConsumedAndSliderComposable(
    calorieDescription: String,
    shouldEnableSlider: Boolean,
    calories: Float,
    textModifier: Modifier,
    sliderModifier: Modifier,
    onCaloriesChanged: (Float) -> Unit = {},
) {
    Text(
        text = calorieDescription,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = textModifier
    )

    Slider(
        value = calories,
        onValueChange = {
            onCaloriesChanged(it)
        },
        valueRange = 1000F..5000F,
        enabled = shouldEnableSlider,
        modifier = sliderModifier
    )
}
