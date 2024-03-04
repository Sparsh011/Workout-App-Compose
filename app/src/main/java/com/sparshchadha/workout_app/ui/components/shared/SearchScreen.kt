package com.sparshchadha.workout_app.ui.components.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.components.ui_state.NoResultsFoundOrErrorDuringSearch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.FoodCard
import com.sparshchadha.workout_app.ui.screens.workout.gym.Exercise
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

private const val TAG = "SearchScreenTagg"

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
    saveExercise: (GymExercisesEntity) -> Unit,
    navController: NavController,
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
        containerColor = scaffoldBackgroundColor,
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
                    exercises = exercises,
                    saveExercise = saveExercise,
                    workoutViewModel = workoutViewModel,
                    navController = navController
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
            workoutViewModel.getExercisesByNameFromApi(searchQuery = searchBarQuery)
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
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
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
                    globalPaddingValues = paddingValues,
                    localPaddingValues = localPaddingValues,
                    message = event.errorMessage
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
    saveExercise: (GymExercisesEntity) -> Unit,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
) {
    workoutUIStateEvent?.let { event ->
        when (event) {
            is WorkoutViewModel.UIEvent.ShowLoader -> {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
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
                    localPaddingValues = localPaddingValues,
                    saveExercise = saveExercise,
                    workoutViewModel = workoutViewModel,
                    navController = navController
                )
            }

            is WorkoutViewModel.UIEvent.ShowError -> {
                NoResultsFoundOrErrorDuringSearch(
                    globalPaddingValues = paddingValues,
                    localPaddingValues = localPaddingValues,
                    message = event.errorMessage
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
            globalPaddingValues = paddingValues,
            localPaddingValues = localPaddingValues
        )
    } else {
        dishes?.items?.let {
            var items by remember {
                mutableStateOf(
                    it.map { item ->
                        mutableStateOf(
                            ExpandDishItem(
                                item = item,
                                isExpanded = false
                            )
                        )
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        top = localPaddingValues.calculateTopPadding()
                    )
            ) {
                items(items.size) { index ->
                    FoodCard(
                        foodItem = items[index].value.item,
                        expandCard = {
                            items = items.mapIndexed { i, item ->
                                if (i == index) {
                                    mutableStateOf(item.value.copy(isExpanded = true))
                                } else {
                                    item
                                }
                            }
                        },
                        collapseCard = {
                            items = items.mapIndexed { i, item ->
                                if (i == index) {
                                    mutableStateOf(item.value.copy(isExpanded = false))
                                } else {
                                    item
                                }
                            }
                        },
                        shouldExpandCard = items[index].value.isExpanded,
                        saveFoodItemWithQuantity = saveFoodItemWithQuantity
                    )
                }
            }

        }
    }
}

@Composable
fun ExerciseSearchResults(
    paddingValues: PaddingValues,
    exercises: GymExercisesDto?,
    localPaddingValues: PaddingValues,
    saveExercise: (GymExercisesEntity) -> Unit,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
) {
    exercises?.let {
        if (it.size == 0) {
            NoResultsFoundOrErrorDuringSearch(
                globalPaddingValues = paddingValues,
                localPaddingValues = localPaddingValues
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        top = localPaddingValues.calculateTopPadding()
                    )
                    .fillMaxSize()
            ) {
                items(it) { exercise ->
                    var shouldShowBottomSheet by remember {
                        mutableStateOf(false)
                    }

                    Exercise(
                        hideBottomSheet = {
                            shouldShowBottomSheet = false
                        },
                        shouldShowBottomSheet = shouldShowBottomSheet,
                        exercise = exercise,
                        saveExercise = saveExercise
                    ) { exerciseToUpdate ->
                        workoutViewModel.updateExerciseDetails(exerciseToUpdate)
                        navController.navigate(UtilityScreen.ExerciseDetailScreen.route)
                    }
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
        border = BorderStroke(1.dp, scaffoldBackgroundColor),
        color = scaffoldBackgroundColor
    ) {
        TextField(
            colors = TextFieldDefaults.colors(
                focusedTextColor = primaryTextColor,
                unfocusedTextColor = primaryTextColor,
                focusedContainerColor = scaffoldBackgroundColor,
                unfocusedContainerColor = scaffoldBackgroundColor,
                disabledContainerColor = scaffoldBackgroundColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MEDIUM_PADDING)
                .clip(RoundedCornerShape(SMALL_PADDING))
                .focusable(enabled = true)
                .focusRequester(focusRequester = focusRequester),
            value = searchBarQuery,
            onValueChange = {
                updateSearchQuery(it)
            },
            placeholder = {
                Text(
                    color = ColorsUtil.primaryDarkGray,
                    text = "Search Here...",
                    overflow = TextOverflow.Ellipsis
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

data class ExpandDishItem(
    val item: FoodItem,
    val isExpanded: Boolean,
)