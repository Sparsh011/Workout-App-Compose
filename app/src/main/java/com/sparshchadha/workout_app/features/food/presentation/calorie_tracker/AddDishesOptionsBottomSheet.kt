package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.util.ColorsUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDishesOptionsBottomSheet(
    hideBottomSheet: () -> Unit,
    navigateToScreen: (String) -> Unit,
    globalPaddingValues: PaddingValues
) {
    ModalBottomSheet(
        onDismissRequest = {
            hideBottomSheet()
        },
        containerColor = ColorsUtil.bottomBarColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = globalPaddingValues.calculateBottomPadding())
        ) {
            BottomSheetItem(
                icon = Icons.Default.Search,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = {
                    navigateToScreen("SearchScreen/food")
                },
                text = "Search Food"
            )

            BottomSheetItem(
                icon = Icons.Default.Add,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = {
                    navigateToScreen(UtilityScreenRoutes.CreateDishScreen.route)
                },
                text = "Create Dish"
            )
        }
    }
}

@Composable
private fun BottomSheetItem(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ColorsUtil.primaryPurple
        )

        Text(
            text = text,
            color = ColorsUtil.primaryTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}