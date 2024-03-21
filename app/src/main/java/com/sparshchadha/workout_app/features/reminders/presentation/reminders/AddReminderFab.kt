package com.sparshchadha.workout_app.features.reminders.presentation.reminders

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sparshchadha.workout_app.util.ColorsUtil

@Composable
fun AddReminderFab(
    showBottomSheetToAddReminder: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            showBottomSheetToAddReminder()
        },
        containerColor = ColorsUtil.primaryPurple,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
        )
    }
}