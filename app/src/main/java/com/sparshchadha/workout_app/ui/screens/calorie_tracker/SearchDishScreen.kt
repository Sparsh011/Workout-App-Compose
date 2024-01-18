package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel

private const val TAG = "SearchDishScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchDishScreen(vm: SearchFoodViewModel, dishes: NutritionalValueDto?) {
    var searchBarQuery by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            MySearchBar(
                searchBarQuery = searchBarQuery,
                context = context,
                focusManager = focusManager,
                updateSearchQuery = {
                    searchBarQuery = it
                },
                searchDish = {
                    vm.updateSearchQuery(searchQuery = searchBarQuery)
                    vm.getFoodItems()
                }
            )

        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            dishes?.items?.let { items ->
//                Log.e(TAG, "SearchDishScreen: here with $it")
                items(
                    items,
                    key = { item ->
                        item.name
                    }
                ) { item ->
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
                        saturatedFat = item.fat_saturated_g.toString()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    searchBarQuery: String,
    context: Context,
    focusManager: FocusManager,
    updateSearchQuery: (String) -> Unit,
    searchDish: () -> Unit,
) {
//    SearchBar(
//        query = searchBarQuery,
//        onQueryChange = {
//            updateSearchQuery(it)
//        },
//        onSearch = {
//            Toast.makeText(context, "Searching for $it", Toast.LENGTH_SHORT).show()
//            searchDish()
//        },
//        active = true,
//        onActiveChange = {
//            // navigate to search dish composable like swiggy
//        },
//        placeholder = {
//            Text(text = "Search Your Dish...", color = ColorsUtil.primaryDarkGray)
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = null,
//                tint = ColorsUtil.primaryDarkGray
//            )
//        },
//        trailingIcon = {
//            Icon(
//                imageVector = Icons.Default.Close,
//                contentDescription = null,
//                modifier = Modifier.clickable {
//                    updateSearchQuery("")
//                    focusManager.clearFocus()
//                },
//                tint = ColorsUtil.primaryDarkGray
//            )
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 10.dp),
//        colors = SearchBarDefaults.colors(
//            containerColor = ColorsUtil.primarySearchBarColor,
//            inputFieldColors = TextFieldDefaults.textFieldColors(
//                focusedTextColor = ColorsUtil.primaryCreamColor
//            )
//        ),
//        shape = RoundedCornerShape(size = 10.dp)
//    ) {
//
//    }

    Surface(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary) // work around for bottom space
    ) {
        TextField(
            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth(),
            value = searchBarQuery,
            onValueChange = {
                updateSearchQuery(it)
            },
            placeholder = {
                Text(
                    color = Color.White,
                    text = "Search"
                )
            },
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {
//                    onSearchClicked(searchQuery)
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
//                    if (searchQuery.isNotEmpty()) {
//                        onTextChange("")
//                    } else {
//                        onCloseClicked()
//                    }
                })
                {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        tint = Color.White
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