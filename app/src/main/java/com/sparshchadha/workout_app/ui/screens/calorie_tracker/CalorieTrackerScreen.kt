package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.HelperFunctions

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    foodItemsConsumedToday: List<FoodItemEntity>?,
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit,
    updateCaloriesGoal: (Int) -> Unit,
    caloriesGoal: String
) {

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
                    caloriesGoal = caloriesGoal,
                    hideCaloriesBottomSheet = {
                        shouldShowCaloriesBottomSheet = false
                    },
                    updateCaloriesGoal = {
                        updateCaloriesGoal(it.toInt())
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
                        text = "No Dishes Consumed!",
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
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit,
) {
    val last30Days = HelperFunctions.getLast30Days()
    last30Days.reverse()

    val lazyRowState = rememberLazyListState()
    LaunchedEffect(key1 = true, block = {
        lazyRowState.scrollToItem(last30Days.size - 3)
    })

    val getNext2Days = HelperFunctions.getNext2Days()

    val (currentDay, currentMonth) = HelperFunctions.getCurrentDateAndMonth()
    var selectedDay by remember {
        mutableIntStateOf(currentDay)
    }
    var selectedMonth by remember {
        mutableStateOf(currentMonth)
    }

    BoxWithConstraints {
        val width = maxWidth / 5

        LazyRow (
            state = lazyRowState
        ){
            items(last30Days) {
                if (selectedDay == it.first && selectedMonth == it.second) {
                    DayAndDate(
                        date = it.first.toString(),
                        month = it.second.substring(0..2),
                        modifier = Modifier
                            .clickable {

                                getDishesConsumedOnSelectedDayAndMonth(Pair(it.first, it.second))
                            }
                            .width(width),
                        isSelected = true
                    )
                } else {
                    DayAndDate(
                        date = it.first.toString(),
                        month = it.second.substring(0..2),
                        modifier = Modifier
                            .clickable {
                                selectedDay = it.first
                                selectedMonth = it.second
                                getDishesConsumedOnSelectedDayAndMonth(Pair(it.first, it.second))
                            }
                            .width(width)
                    )
                }
            }

            items(getNext2Days) {
                DayAndDate(
                    date = it.first.toString(),
                    month = it.second.substring(0..2),
                    modifier = Modifier
                        .width(width),
                    monthColor = ColorsUtil.unselectedBottomBarIconColor,
                    dateColor = ColorsUtil.unselectedBottomBarIconColor
                )
            }
        }
    }


}

@Composable
fun DayAndDate(
    date: String,
    month: String,
    modifier: Modifier = Modifier,
    monthColor: Color = Color.Black,
    dateColor: Color = ColorsUtil.primaryDarkTextColor,
    isSelected: Boolean = false
) {
    if (isSelected) {
        Column(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ColorsUtil.primaryDarkTextColor)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = month,
                fontSize = TextUnit(20f, TextUnitType.Unspecified),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = date,
                fontSize = TextUnit(18f, TextUnitType.Unspecified),
                color = Color.White
            )
        }
    } else {
        Column(
            modifier = modifier
                .background(Color.White)
                .padding(10.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = month,
                fontSize = TextUnit(20f, TextUnitType.Unspecified),
                fontWeight = FontWeight.Bold,
                color = monthColor
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = date,
                fontSize = TextUnit(18f, TextUnitType.Unspecified),
                color = dateColor
            )
        }
    }
}



@Composable
fun CaloriesConsumedAndSliderComposable(
    calorieDescription: String,
    shouldEnableSlider: Boolean,
    calories: String,
    textModifier: Modifier,
    sliderModifier: Modifier,
    onCaloriesChanged: (Float) -> Unit = {},
) {
    Text(
        text = calorieDescription,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = textModifier
    )

    Slider(
        value = calories.toFloat(),
        onValueChange = {
            onCaloriesChanged(it)
        },
        valueRange = 1000F..5000F,
        enabled = shouldEnableSlider,
        modifier = sliderModifier
    )
}
