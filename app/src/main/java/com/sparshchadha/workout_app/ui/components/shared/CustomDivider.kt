package com.sparshchadha.workout_app.ui.components.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING

@Composable
fun CustomDivider(
    modifier: Modifier = Modifier,
    dividerColor: Color = ColorsUtil.dividerColor,
    verticalPadding: Dp = SMALL_PADDING,
    horizontalPadding: Dp = MEDIUM_PADDING
) {
    HorizontalDivider(
        modifier = Modifier
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .then(modifier),
        thickness = 1.dp,
        color = dividerColor
    )
}