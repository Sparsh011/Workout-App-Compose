package com.sparshchadha.workout_app.features.profile.presentation.profile.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun SettingsCategory(
    text: String,
    onClick: () -> Unit,
    verticalLineColor: Color,
    showDivider: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.nonScaledSp,
                color = primaryTextColor,
                modifier = Modifier
                    .padding(MEDIUM_PADDING)
                    .weight(4f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = primaryBlue,
                modifier = Modifier.weight(1f)
            )
        }

        if (showDivider) CustomDivider()
    }
}

@Composable
fun SettingsCategoryHeader(
    text: String,
    onClick: () -> Unit,
    isVisible: Boolean
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth().clickable {
            onClick()
        }
    ){
        Text(
            text = text,
            modifier = Modifier
                .fillMaxSize()
                .padding(MEDIUM_PADDING)
                .weight(4f)
                ,
            color = primaryTextColor,
            fontSize = 20.nonScaledSp,
            fontWeight = FontWeight.Bold
        )

        Icon(
            imageVector = if (isVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = ColorsUtil.primaryBlue,
            modifier = Modifier
                .weight(1f)
        )
    }
}