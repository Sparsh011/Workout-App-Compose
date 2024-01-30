package com.sparshchadha.workout_app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.ui.components.ui_state.NoResultsFoundOrErrorDuringSearch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.FoodCard
import com.sparshchadha.workout_app.ui.screens.workout.gym.Exercise
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun SearchScreen(
    searchFoodViewModel: FoodItemsViewModel,
    paddingValues: PaddingValues,
    onCloseClicked: () -> Unit,
    searchFor: String?,
    workoutViewModel: WorkoutViewModel,
    dishes: NutritionalValueDto?,
    exercises: GymExercisesDto?,
    workoutUIStateEvent: WorkoutViewModel.UIEvent?,
    foodUIStateEvent: WorkoutViewModel.UIEvent?,
    saveFoodItemWithQuantity: (FoodItemEntity) -> Unit,
    ) {
    var searchBarQuery by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit, block = {
        focusRequester.requestFocus()
    })

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
                    handleSearchFor(
                        searchFor = searchFor,
                        searchBarQuery = searchBarQuery,
                        searchFoodViewModel = searchFoodViewModel,
                        workoutViewModel = workoutViewModel
                    )
                },
                onCloseClicked = onCloseClicked
            )
        }
    ) { localPaddingValues ->
        when (searchFor) {
            "food" -> {
                HandleFoodSearch(
                    foodUIStateEvent = foodUIStateEvent,
                    paddingValues = paddingValues,
                    localPaddingValues = localPaddingValues,
                    dishes = dishes,
                    saveFoodItemWithQuantity = saveFoodItemWithQuantity
                )
            }

            "exercises" -> {
                HandleExercisesSearch(
                    workoutUIStateEvent = workoutUIStateEvent,
                    paddingValues = paddingValues,
                    localPaddingValues = localPaddingValues,
                    exercises = exercises
                )
            }
        }
    }
}

fun handleSearchFor(
    searchFor: String?,
    searchBarQuery: String,
    searchFoodViewModel: FoodItemsViewModel,
    workoutViewModel: WorkoutViewModel,
) {
    when (searchFor) {
        "food" -> {
            searchFoodViewModel.updateSearchQuery(searchQuery = searchBarQuery)
            searchFoodViewModel.getFoodItemsFromApi()
        }

        "exercises" -> {
            workoutViewModel.getExercisesByName(searchQuery = searchBarQuery)
        }
    }
}

@Composable
fun HandleFoodSearch(
    foodUIStateEvent: WorkoutViewModel.UIEvent?,
    paddingValues: PaddingValues,
    localPaddingValues: PaddingValues,
    dishes: NutritionalValueDto?,
    saveFoodItemWithQuantity: (FoodItemEntity) -> Unit,
    ) {
    foodUIStateEvent?.let { event ->
        when (event) {
            is WorkoutViewModel.UIEvent.ShowLoader -> {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.food_search_animation))
                val progress by animateLottieCompositionAsState(composition)
                ShowLoadingScreen(
                    composition = composition,
                    progress = progress
                )
            }

            is WorkoutViewModel.UIEvent.HideLoaderAndShowResponse -> {
                FoodSearchResults(
                    paddingValues = paddingValues,
                    dishes = dishes,
                    localPaddingValues = localPaddingValues,
                    saveFoodItemWithQuantity = saveFoodItemWithQuantity
                )
            }

            is WorkoutViewModel.UIEvent.ShowError -> {
                NoResultsFoundOrErrorDuringSearch(
                    paddingValues = paddingValues,
                    localPaddingValues = localPaddingValues,
                    errorMessage = event.errorMessage
                )
            }
        }
    }
}

@Composable
fun HandleExercisesSearch(
    workoutUIStateEvent: WorkoutViewModel.UIEvent?,
    paddingValues: PaddingValues,
    localPaddingValues: PaddingValues,
    exercises: GymExercisesDto?,
) {
    workoutUIStateEvent?.let { event ->
        when (event) {
            is WorkoutViewModel.UIEvent.ShowLoader -> {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gym_exercises_animation))
                val progress by animateLottieCompositionAsState(composition)
                ShowLoadingScreen(
                    composition = composition,
                    progress = progress
                )
            }

            is WorkoutViewModel.UIEvent.HideLoaderAndShowResponse -> {
                ExerciseSearchResults(
                    paddingValues = paddingValues,
                    exercises = exercises,
                    localPaddingValues = localPaddingValues
                )
            }

            is WorkoutViewModel.UIEvent.ShowError -> {
                NoResultsFoundOrErrorDuringSearch(
                    paddingValues = paddingValues,
                    localPaddingValues = localPaddingValues,
                    errorMessage = event.errorMessage
                )
            }
        }
    }
}

@Composable
fun FoodSearchResults(
    paddingValues: PaddingValues,
    dishes: NutritionalValueDto?,
    localPaddingValues: PaddingValues,
    saveFoodItemWithQuantity: (FoodItemEntity) -> Unit,
) {
    if (dishes?.items?.isEmpty() == true) {
        NoResultsFoundOrErrorDuringSearch(
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
                        foodItem = item,
                        expandCard = {
                            shouldExpandCard = true
                        },
                        collapseCard = {
                            shouldExpandCard = false
                        },
                        shouldExpandCard = shouldExpandCard,
                        saveFoodItemWithQuantity = saveFoodItemWithQuantity
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSearchResults(paddingValues: PaddingValues, exercises: GymExercisesDto?, localPaddingValues: PaddingValues) {
    exercises?.let {
        if (it.size == 0) {
            NoResultsFoundOrErrorDuringSearch(
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
fun MySearchBar(
    searchBarQuery: String,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    updateSearchQuery: (String) -> Unit,
    searchDish: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    Surface(
        border = BorderStroke(2.dp, Color.White),
        color = Color.White
    ) {
        TextField(
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = ColorsUtil.primaryLightGray,
                unfocusedContainerColor = ColorsUtil.primaryLightGray,
                disabledContainerColor = ColorsUtil.primaryLightGray,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .focusable(enabled = true)
                .focusRequester(focusRequester = focusRequester),
            value = searchBarQuery,
            onValueChange = {
                updateSearchQuery(it)
            },
            placeholder = {
                Text(
                    color = ColorsUtil.primaryDarkGray,
                    text = "Search Here..."
                )
            },
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {
                        searchDish()
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        tint = Color.Gray,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (searchBarQuery.isNotEmpty()) {
                            updateSearchQuery("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        tint = Color.Gray
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