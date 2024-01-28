package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.util.Log
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
import com.sparshchadha.workout_app.util.ColorsUtil
import kotlinx.coroutines.launch

private const val TAG = "CalorieTrackerScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieTrackerScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
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
                SelectDay()
            }

            // Today's consumed dishes -
            item {
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

            items(listOf("Cake", "Pasta", "Pizza", "Sandwich", "Burger", "Paneer")) {
                FoodCard(
                    foodItemName = it,
                    calories = "100",
                    sugar = "100",
                    fiber = "100",
                    sodium = "100",
                    cholesterol = "100",
                    protein = "100",
                    carbohydrates = "100",
                    servingSize = "100",
                    totalFat = "100",
                    saturatedFat = "100",
                    expandCard = { /*TODO*/ },
                    collapseCard = { /*TODO*/ },
                    shouldExpandCard = false
                )
            }
        }
    }
}

@Composable
fun SelectDay() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val list = listOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
    )
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            listState.scrollToItem(list.size / 2)
        }
    }

    var selectedItem by remember {
        mutableIntStateOf(list.size / 2)
    }

    val visibleItems by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                0
            } else {
                val firstVisibleItem = visibleItemsInfo.first()
                val lastVisibleItem = visibleItemsInfo.last()
                lastVisibleItem.index - firstVisibleItem.index
            }
        }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        state = listState,
        horizontalArrangement = Arrangement.Center
    ) {
        items(list.size) {
            if (it == selectedItem) {
                DayAndDate(
                    date = it.toString(),
                    day = "Mon",
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorsUtil.primaryGreenCardBackground)
                        .padding(10.dp)
                        .fillParentMaxWidth(0.1f)
                        .clickable {
                            selectedItem = it
                            coroutineScope.launch {
                                if (it < 2 || it - (visibleItems / 2) < 0) {
                                    listState.scrollToItem(it)
                                } else {
                                    listState.scrollToItem(it - (visibleItems / 2))
                                }
                            }
                        },
                )

            } else {
                DayAndDate(
                    date = it.toString(),
                    day = "Tues",
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(10.dp)
                        .fillParentMaxWidth(0.1f)
                        .clickable {
                            selectedItem = it
                            coroutineScope.launch {
                                if (it - (visibleItems / 2) < 0) {
                                    listState.scrollToItem(it)
                                } else {
                                    listState.scrollToItem(it - (visibleItems / 2))
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun DayAndDate(
    date: String,
    day: String,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day,
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
