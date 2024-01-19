package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sparshchadha.workout_app.data.remote.dto.food_api.Item
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel

private const val TAG = "SearchDishScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchDishScreen(
    searchFoodViewModel: SearchFoodViewModel,
    paddingValues: PaddingValues,
    onCloseClicked: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color.White
    )

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
                    searchFoodViewModel.updateSearchQuery(searchQuery = searchBarQuery)
                    searchFoodViewModel.getFoodItems()
                },
                onCloseClicked = onCloseClicked
            )
        }
    ) { localPaddingValues ->
        val dishesMap = remember {
            mutableStateMapOf<Item, Boolean>()
        }
        dishes?.items?.let {
            it.forEach { item ->
                dishesMap[item] = false
            }
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding(), top = localPaddingValues.calculateTopPadding())
        ) {
            dishes?.items?.let { items ->
//                Log.e(TAG, "SearchDishScreen: here with $it")
                items(
                    items,
                    key = { item ->
                        item.name
                    }
                ) { item ->
                    dishesMap[item]?.let {
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
                                dishesMap[item] = true
                            },
                            collapseCard = {
                                dishesMap[item] = false
                            },
                            expandCardState = it
                        )
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
    onCloseClicked: () -> Unit
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