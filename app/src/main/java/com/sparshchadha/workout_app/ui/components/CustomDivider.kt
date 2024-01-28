package com.sparshchadha.workout_app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.util.ColorsUtil

@Composable
fun CustomDivider() {
    Divider(
        color = ColorsUtil.primaryLightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    )
}