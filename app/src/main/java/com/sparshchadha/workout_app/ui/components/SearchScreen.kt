package com.sparshchadha.workout_app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDto
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.FoodCard
import com.sparshchadha.workout_app.ui.screens.workout.gym.Exercise
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun SearchScreen(
    searchFoodViewModel: SearchFoodViewModel,
    paddingValues: PaddingValues,
    onCloseClicked: () -> Unit,
    searchFor: String?,
    workoutViewModel: WorkoutViewModel,
) {
    var searchBarQuery by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit, block = {
        focusRequester.requestFocus()
    })

    val dishes = searchFoodViewModel.foodItems.value

    Scaffold(
        containerColor = Color.White,
        topBar = {
            MySearchBar(
                searchBarQuery = searchBarQuery,
                focusManager = focusManager,
                focusRequester = focusRequester,

                updateSearchQuery = {
                    searchBarQuery = it
                },
                searchDish = {
                    when (searchFor) {
                        "food" -> {
                            searchFoodViewModel.updateSearchQuery(searchQuery = searchBarQuery)
                            searchFoodViewModel.getFoodItems()
                        }

                        "exercises" -> {
                            workoutViewModel.getExercisesByName(searchQuery = searchBarQuery)
                        }
                    }
                },
                onCloseClicked = onCloseClicked
            )
        }
    ) { localPaddingValues ->
        when (searchFor) {
            "food" -> {
                FoodSearchResults(paddingValues = paddingValues, dishes = dishes, localPaddingValues = localPaddingValues)
            }

            "exercises" -> {
                val exercises = workoutViewModel.exercises.value
                ExerciseSearchResults(
                    paddingValues = paddingValues,
                    exercises = exercises,
                    localPaddingValues = localPaddingValues
                )
            }
        }
    }
}

@Composable
fun FoodSearchResults(paddingValues: PaddingValues, dishes: NutritionalValueDto?, localPaddingValues: PaddingValues) {
    if (dishes?.items?.isEmpty() == true) {
        NoResultsFound(
            paddingValues = paddingValues,
            localPaddingValues = localPaddingValues
        )
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding(), top = localPaddingValues.calculateTopPadding())
        ) {
            dishes?.items?.let { items ->
                items(
                    items
                ) { item ->
                    var shouldExpandCard by remember {
                        mutableStateOf(false)
                    }

                    FoodCard(
                        foodItemName = item.name.capitalize(),
                        calories = item.calories.toString(),
                        sugar = item.sugar_g.toString(),
                        fiber = item.fiber_g.toString(),
                        sodium = item.sodium_mg.toString(),
                        cholesterol = item.cholesterol_mg.toString(),
                        protein = item.protein_g.toString(),
                        carbohydrates = item.carbohydrates_total_g.toString(),
                        servingSize = item.serving_size_g.toString(),
                        totalFat = item.fat_total_g.toString(),
                        saturatedFat = item.fat_saturated_g.toString(),
                        expandCard = {
                            shouldExpandCard = true
                        },
                        collapseCard = {
                            shouldExpandCard = false
                        },
                        shouldExpandCard = shouldExpandCard
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSearchResults(paddingValues: PaddingValues, exercises: GymWorkoutsDto?, localPaddingValues: PaddingValues) {
    exercises?.let {
        if (it.size == 0) {
            NoResultsFound(
                paddingValues = paddingValues,
                localPaddingValues = localPaddingValues
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding(), top = localPaddingValues.calculateTopPadding())
                    .fillMaxSize()
            ) {
                items(it) { exercise ->
                    var shouldShowBottomSheet by remember {
                        mutableStateOf(false)
                    }

                    val sheetState = rememberModalBottomSheetState()

                    Exercise(
                        name = exercise.name,
                        difficulty = exercise.difficulty,
                        equipment = exercise.equipment,
                        muscle = exercise.muscle,
                        instructions = exercise.instructions,
                        showBottomSheet = {
                            shouldShowBottomSheet = true
                        },
                        hideBottomSheet = {
                            shouldShowBottomSheet = false
                        },
                        shouldShowBottomSheet = shouldShowBottomSheet,
                        sheetState = sheetState
                    )
                }
            }
        }
    }
}

@Composable
fun NoResultsFound(paddingValues: PaddingValues, localPaddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding(), top = localPaddingValues.calculateTopPadding())
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_results_found_animation))
        val progress by animateLottieCompositionAsState(composition)

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.padding(20.dp)
        )

        Text(
            text = "No Results Found!",
            color = Color.Black,
            modifier = Modifier.padding(20.dp)
                .fillMaxWidth(),
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MySearchBar(
    searchBarQuery: String,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    updateSearchQuery: (String) -> Unit,
    searchDish: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    Surface(
        border = BorderStroke(2.dp, Color.White)
    ) {
        TextField(
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusable(enabled = true)
                .focusRequester(focusRequester = focusRequester),
            value = searchBarQuery,
            onValueChange = {
                updateSearchQuery(it)
            },
            placeholder = {
                Text(
                    color = Color.Black,
                    text = "Search..."
                )
            },
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {
                    searchDish()
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        tint = Color.Gray,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (searchBarQuery.isNotEmpty()) {
                        updateSearchQuery("")
                    } else {
                        onCloseClicked()
                    }
                })
                {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search // keyboard's bottom right button will be search button
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchDish()
                    focusManager.clearFocus() // close keyboard when search button is clicked from keyboard
                }
            )
        )
    }
}