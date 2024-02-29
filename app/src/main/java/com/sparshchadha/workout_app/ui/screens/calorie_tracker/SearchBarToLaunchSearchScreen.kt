package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.util.ColorsUtil.primaryGreenCardBackground
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.statusBarColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarToLaunchSearchScreen(
    searchBarQuery: String,
    updateSearchBarQuery: (String) -> Unit,
    navController: NavHostController,
) {
    SearchBar(
        query = searchBarQuery,
        onQueryChange = {
            updateSearchBarQuery(it)
        },
        onSearch = {

        },
        active = false,
        onActiveChange = {
            navController.navigate("SearchScreen/food")
        },
        placeholder = {
            Text(text = "Search Dishes...", color = primaryTextColor, fontSize = 18.nonScaledSp)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = primaryTextColor
            )
        },
        trailingIcon = { },
        modifier = Modifier
            .fillMaxWidth()
            .background(statusBarColor)
            .padding(horizontal = Dimensions.MEDIUM_PADDING),
        colors = SearchBarDefaults.colors(
            containerColor = statusBarColor,
            inputFieldColors = TextFieldDefaults.textFieldColors(
                focusedTextColor = primaryGreenCardBackground
            )
        ),
        shape = RoundedCornerShape(size = 10.dp)
    ) {

    }
}