package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkGray
import com.sparshchadha.workout_app.util.ColorsUtil.primaryGreenCardBackground
import com.sparshchadha.workout_app.util.ColorsUtil.primaryLightGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarToLaunchSearchScreen(
    searchBarQuery: String,
    updateSearchBarQuery: (String) -> Unit,
    context: Context,
    navController: NavHostController,
    focusManager: FocusManager,
) {
    SearchBar(
        query = searchBarQuery,
        onQueryChange = {
            updateSearchBarQuery(it)
        },
        onSearch = {
            Toast.makeText(context, "Searching for $it", Toast.LENGTH_SHORT).show()
        },
        active = false,
        onActiveChange = {
            navController.navigate("SearchScreen/food")
        },
        placeholder = {
            Text(text = "Search Dishes...", color = primaryDarkGray)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = primaryDarkGray
            )
        },
        trailingIcon = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        colors = SearchBarDefaults.colors(
            containerColor = primaryLightGray,
            inputFieldColors = TextFieldDefaults.textFieldColors(
                focusedTextColor = primaryGreenCardBackground
            )
        ),
        shape = RoundedCornerShape(size = 10.dp)
    ) {

    }
}