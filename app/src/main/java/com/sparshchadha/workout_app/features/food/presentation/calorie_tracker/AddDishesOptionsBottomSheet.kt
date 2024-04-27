package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDishesOptionsDialog(
    hideDialog: () -> Unit,
    navigateToScreen: (String) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { hideDialog() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                .background(ColorsUtil.bottomBarColor)
                .padding(Dimensions.LARGE_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomSheetItem(
                icon = Icons.Default.Search,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                onClick = {
                    navigateToScreen("SearchScreen/food")
                },
                text = "Search Food"
            )

            BottomSheetItem(
                icon = Icons.Default.Add,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
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