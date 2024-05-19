package com.sparshchadha.workout_app.features.profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun AppSettings(
    setDarkTheme: (Boolean) -> Unit,
    isDarkTheme: Boolean?,
    onAuthButtonClick: () -> Unit,
    loginToken: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING)
            .clip(RoundedCornerShape(SMALL_PADDING))
            .background(bottomBarColor)
    ) {

        AppSettingCategory(
            text = "Enable Dark Theme",
            showToggleSwitch = true,
            isDarkTheme = isDarkTheme,
            setDarkTheme = setDarkTheme,
            textColor = primaryTextColor,
            fontWeight = FontWeight.Normal
        )

        AppSettingCategory(
            text = "Rate On Playstore",
            showToggleSwitch = false,
            isDarkTheme = isDarkTheme,
            setDarkTheme = setDarkTheme,
            textColor = primaryTextColor,
            fontWeight = FontWeight.Normal,
            onClick = {

            }
        )

        AppSettingCategory(
            text = if (loginToken.isNotBlank()) "Sign Out" else "Sign In",
            showToggleSwitch = false,
            isDarkTheme = isDarkTheme,
            setDarkTheme = setDarkTheme,
            textColor = if (loginToken.isNotBlank()) noAchievementColor else targetAchievedColor,
            fontWeight = FontWeight.Normal,
            onClick = onAuthButtonClick,
        )
    }
}

@Composable
fun AppSettingCategory(
    text: String,
    showToggleSwitch: Boolean,
    isDarkTheme: Boolean?,
    setDarkTheme: (Boolean) -> Unit,
    textColor: Color,
    fontWeight: FontWeight,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.nonScaledSp,
            color = textColor,
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .weight(4f),
            fontWeight = fontWeight
        )

        if (showToggleSwitch) {
            Switch(
                checked = isDarkTheme ?: isSystemInDarkTheme(),
                onCheckedChange = {
                    setDarkTheme(it)
                },
                modifier = Modifier
                    .padding(SMALL_PADDING)
                    .weight(1f),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = primaryBlue
                )
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = primaryBlue,
                modifier = Modifier.weight(1f)
            )
        }
    }
}