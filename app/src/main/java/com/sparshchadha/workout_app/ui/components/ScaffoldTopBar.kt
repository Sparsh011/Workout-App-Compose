package com.sparshchadha.workout_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.statusBarColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun ScaffoldTopBar(
    topBarDescription: String,
    onBackButtonPressed: () -> Unit = {},
    showBackIcon: Boolean = true,
    topBarHorizontalPadding: Dp = MEDIUM_PADDING,
    topBarVerticalPadding: Dp = MEDIUM_PADDING,
) {
    Row(
        modifier = Modifier
            .background(statusBarColor)
            .fillMaxWidth()
            .padding(
                horizontal = topBarHorizontalPadding,
                vertical = topBarVerticalPadding
            )
            .background(statusBarColor)
    ) {
        if (showBackIcon) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = primaryTextColor,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = MEDIUM_PADDING)
                    .clickable {
                        onBackButtonPressed()
                    }
            )
        }

        Text(
            text = topBarDescription,
            color = primaryTextColor,
            fontSize = 22.nonScaledSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = Dimensions.LARGE_PADDING)
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
    }
}