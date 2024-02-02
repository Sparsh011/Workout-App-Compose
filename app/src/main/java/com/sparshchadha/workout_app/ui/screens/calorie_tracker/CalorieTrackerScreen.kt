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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.ui.components.CalendarRow
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.customDividerColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkTextColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel

private const val TAG = "CalorieTrackerScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    getDishesConsumedOnSelectedDayAndMonth: (Pair<Int, String>) -> Unit,
    saveNewCaloriesGoal: (Float) -> Unit,
    caloriesGoal: String,
    caloriesConsumed: String,
    selectedDateAndMonth: Pair<Int, String>?,
    removeFoodItem: (FoodItemEntity) -> Unit,
    foodItemsViewModel: FoodItemsViewModel,
) {

    val foodItemsConsumed = foodItemsViewModel.savedFoodItems.collectAsState().value
    val nutrientsConsumed = foodItemsViewModel.nutrientsConsumed

    var shouldShowCaloriesBottomSheet by remember {
        mutableStateOf(false)
    }
    val updateCaloriesBottomSheetState = rememberModalBottomSheetState()

    var searchBarQuery by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            SearchBarToLaunchSearchScreen(
                searchBarQuery = searchBarQuery,
                updateSearchBarQuery = {
                    searchBarQuery = it
                },
                navController = navController
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

                            1 -> {
                                MacroNutrientsConsumed(
                                    nutrientsConsumed = nutrientsConsumed
                                )
                            }
                        }
                    }

                    CurrentlySelectedCard(currentPage = pagerState.currentPage)
                }
            }

            // Select day to show that day's calories and dishes consumed
            item {
                CalendarRow(
                    getResultsForDateAndMonth = getDishesConsumedOnSelectedDayAndMonth,
                    selectedMonth = selectedDateAndMonth?.second ?: "January",
                    selectedDay = selectedDateAndMonth?.first ?: 1,
                    indicatorColor = HelperFunctions.getAchievementColor(
                        achieved = caloriesConsumed.toInt(),
                        target = caloriesGoal.toInt()
                    )
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
                        animationModifier = Modifier.size(
                            Dimensions.LOTTIE_ANIMATION_SIZE_LARGE
                        ),
                        textSize = 20.nonScaledSp
                    )
                }

            } else {
                items(foodItemsConsumed, key = { foodItem ->
                    foodItem.id.toString()
                }) { foodItem ->
                    var shouldShowFoodItemDetails by remember {
                        mutableStateOf(false)
                    }

                    PopulateConsumedFoodItem(
                        consumedFoodItem = foodItem,
                        showFoodItemDetails = {
                            shouldShowFoodItemDetails = true
                        },
                        removeFoodItem = removeFoodItem
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

@Composable
fun CurrentlySelectedCard(currentPage: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = Dimensions.SMALL_PADDING
            ),
        horizontalArrangement = Arrangement.Center
    ) {

        if (currentPage == 0) {
            Row {
                Canvas(
                    modifier = Modifier
                        .padding(Dimensions.SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = Red)
                }

                Canvas(
                    modifier = Modifier
                        .padding(Dimensions.SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = customDividerColor)
                }
            }

        } else {
            Row {
                Canvas(
                    modifier = Modifier
                        .padding(Dimensions.SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = customDividerColor)
                }

                Canvas(
                    modifier = Modifier
                        .padding(Dimensions.SMALL_PADDING)
                        .size(Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE)

                ) {
                    drawCircle(color = Red)
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
                    .padding(Dimensions.MEDIUM_PADDING)
                    .fillMaxWidth()
            ) {
                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    title = foodItem.foodItemDetails?.name?.capitalize() ?: "Sorry, Unable To Get Name!"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Servings:",
                    quantityOfMacroNutrient = foodItem.servings.toString()
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Calories Per Serving: ",
                    quantityOfMacroNutrient = foodItem.foodItemDetails?.calories.toString() + " KCAL"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Total Calories: ",
                    quantityOfMacroNutrient = (foodItem.servings * (foodItem.foodItemDetails?.calories?.toInt()
                        ?: 0)).toString() + " KCAL"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Carbohydrates:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.carbohydrates_total_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Protein:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.protein_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Total Fat:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.fat_total_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Saturated Fat:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.fat_saturated_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Sugar:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.sugar_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Cholesterol:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.cholesterol_mg} mg"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Sodium:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.sodium_mg} mg"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
                    macroNutrient = "Fiber:",
                    quantityOfMacroNutrient = "${foodItem.foodItemDetails?.fiber_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.MEDIUM_PADDING),
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
            color = primaryDarkTextColor,
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
                    .padding(Dimensions.MEDIUM_PADDING),
                fontWeight = FontWeight.Bold,
                color = primaryDarkTextColor,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = quantityOfMacroNutrient,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimensions.MEDIUM_PADDING),
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Normal,
                color = primaryDarkTextColor,
                overflow = TextOverflow.Ellipsis
            )
        }

        CustomDivider()
    }
}

@Composable
fun PopulateConsumedFoodItem(
    consumedFoodItem: FoodItemEntity,
    showFoodItemDetails: () -> Unit,
    removeFoodItem: (FoodItemEntity) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MEDIUM_PADDING)
            .clickable {
                showFoodItemDetails()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.SMALL_PADDING)
        ) {
            Column(
                modifier = Modifier.weight(4f)
            ) {
                consumedFoodItem.foodItemDetails?.name?.let { itemName ->
                    Text(
                        text = itemName.capitalize(),
                        color = primaryDarkTextColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.nonScaledSp,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "${(consumedFoodItem.servings * (consumedFoodItem.foodItemDetails?.calories ?: 0).toInt())} KCAL",
                    color = primaryDarkTextColor,
                    modifier = Modifier
                        .padding(5.dp),
                    fontSize = 14.nonScaledSp,
                    overflow = TextOverflow.Ellipsis
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
                    color = primaryDarkTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.nonScaledSp,
                )
            ) {
                append("Consumed")
            }
        },
        fontSize = 20.nonScaledSp,
        color = primaryDarkTextColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(15.dp),
        overflow = TextOverflow.Ellipsis
    )
}

