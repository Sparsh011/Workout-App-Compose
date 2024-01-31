package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    foodItemsConsumed: List<FoodItemEntity>?,
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit,
    saveNewCaloriesGoal: (Float) -> Unit,
    caloriesGoal: String,
    caloriesConsumed: String,
) {

    var shouldShowCaloriesBottomSheet by remember {
        mutableStateOf(false)
    }
    val updateCaloriesBottomSheetState = rememberModalBottomSheetState()

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
                    sheetState = updateCaloriesBottomSheetState,
                    caloriesGoal = caloriesGoal,
                    hideCaloriesBottomSheet = {
                        shouldShowCaloriesBottomSheet = false
                    },
                    saveNewCaloriesGoal = saveNewCaloriesGoal,
                    showCaloriesGoalBottomSheet = {
                        shouldShowCaloriesBottomSheet = true
                    },
                    caloriesConsumed = caloriesConsumed
                )
            }

            // Select day to show that day's calories and dishes consumed
            item {
                SelectDay(
                    getDishesConsumedOnSelectedDayAndMonth = getDishesConsumedOnSelectedDayAndMonth
                )
            }

            // Dishes consumed on header
            item {
                DishesConsumedOnAParticularDayHeader()
            }

            // Dishes consumed on a particular day -
            if (foodItemsConsumed.isNullOrEmpty()) {
                item {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_food_item_added_animation))
                    val progress by animateLottieCompositionAsState(composition)

                    NoWorkoutPerformedOrFoodConsumed(
                        composition = composition,
                        progress = progress,
                        animationModifier = Modifier.size(200.dp),
                        textSize = 20.sp
                    )
                }

            } else {
                items(foodItemsConsumed) { foodItem ->
                    var shouldShowFoodItemDetails by remember {
                        mutableStateOf(false)
                    }

                    PopulateConsumedFoodItem(
                        consumedFoodItem = foodItem,
                        showFoodItemDetails = {
                            shouldShowFoodItemDetails = true
                        }
                    )

                    if (shouldShowFoodItemDetails) {
                        FoodItemDialogBox(
                            foodItem,
                            hideFoodItemDialogBox = {
                                shouldShowFoodItemDetails = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItemDialogBox(
    foodItem: FoodItemEntity,
    hideFoodItemDialogBox: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = Color.White,
        onDismissRequest = {
            hideFoodItemDialogBox()
        },
        content = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    title = foodItem.foodItemDetails?.name?.capitalize() ?: "Sorry, Unable To Get Name!"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Servings:",
                    quantityOfMacroNutrient = foodItem.servings.toString()
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Calories Per Serving: ",
                    quantityOfMacroNutrient = foodItem.foodItemDetails?.calories.toString() + " KCAL"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Total Calories: ",
                    quantityOfMacroNutrient =  (foodItem.servings * (foodItem.foodItemDetails?.calories?.toInt() ?: 0)).toString() + " KCAL"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Carbohydrates:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.carbohydrates_total_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Protein:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.protein_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Total Fat:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.fat_total_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Saturated Fat:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.fat_saturated_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Sugar:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.sugar_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Cholesterol:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.cholesterol_mg} mg"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Sodium:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.sodium_mg} mg"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Fiber:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.fiber_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    macroNutrient = "Potassium:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.potassium_mg} mg"
                )
            }
        }
    )
}

@Composable
fun FoodItemText(
    modifier: Modifier = Modifier,
    title: String = "",
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
            color = ColorsUtil.primaryDarkTextColor
        )
    } else {
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = macroNutrient,
                textAlign = TextAlign.Start,
                fontSize = 18.nonScaledSp,
                modifier = Modifier
                    .weight(2f)
                    .padding(10.dp),
                fontWeight = FontWeight.Bold,
                color = ColorsUtil.primaryDarkTextColor
            )

            Text(
                text = quantityOfMacroNutrient,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Normal,
                color = ColorsUtil.primaryDarkTextColor
            )
        }

        CustomDivider()
    }
}

@Composable
fun PopulateConsumedFoodItem(
    consumedFoodItem: FoodItemEntity,
    showFoodItemDetails: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                showFoodItemDetails()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            Column(
                modifier = Modifier.weight(4f)
            ) {
                consumedFoodItem.foodItemDetails?.name?.let { itemName ->
                    Text(
                        text = itemName.capitalize(),
                        color = ColorsUtil.primaryDarkTextColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.nonScaledSp,
                        textAlign = TextAlign.Start
                    )
                }

                Text(
                    text = "${(consumedFoodItem.servings * (consumedFoodItem.foodItemDetails?.calories ?: 0).toInt())} KCAL",
                    color = ColorsUtil.primaryDarkTextColor,
                    modifier = Modifier
                        .padding(5.dp),
                    fontSize = 14.nonScaledSp,
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically),
                tint = ColorsUtil.primaryDarkTextColor
            )
        }

        CustomDivider()
    }
}


@Composable
fun DishesConsumedOnAParticularDayHeader() {
    Text(
        buildAnnotatedString {
            append("Dishes ")
            withStyle(
                style = SpanStyle(
                    color = ColorsUtil.primaryDarkTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.nonScaledSp,
                )
            ) {
                append("Consumed")
            }
        },
        fontSize = 20.nonScaledSp,
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

        LazyRow(
            state = lazyRowState
        ) {
            items(last30Days) {
                if (
                    selectedDayAndMonth(
                        selectedDay = selectedDay,
                        selectedMonth = selectedMonth,
                        pair = it
                    )
                ) {
                    DayAndDate(
                        isSelected = true,
                        date = it.first.toString(),
                        month = it.second.substring(0..2),
                        modifier = Modifier
                            .clickable {

                                getDishesConsumedOnSelectedDayAndMonth(Pair(it.first, it.second))
                            }
                            .width(width)
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

private fun selectedDayAndMonth(selectedDay: Int, selectedMonth: String, pair: Pair<Int, String>): Boolean {
    return selectedDay == pair.first && selectedMonth == pair.second
}

@Composable
fun DayAndDate(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    date: String,
    month: String,
    monthColor: Color = Color.Black,
    dateColor: Color = ColorsUtil.primaryDarkTextColor,
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
                .padding(10.dp),
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
