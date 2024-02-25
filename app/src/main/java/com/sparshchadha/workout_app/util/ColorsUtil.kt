package com.sparshchadha.workout_app.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray

object ColorsUtil {
    private val primaryDarkColor = Color(37, 43, 54)
    val primaryBlue: Color = Color(79, 124, 214, 255)
    val primaryGreen: Color = Color(0, 255, 135)
    val primaryDarkGray: Color = Color(97, 103, 122)
    val primaryLightGray: Color = Color(249, 249, 250, 255)
    val primaryGreenCardBackground: Color = Color(39, 255, 147, 255)
    private val primaryBlack: Color = Color(21, 23, 31, 255)
    val unselectedBottomBarIconColor: Color = Color(139, 143, 156, 255)
    val customDividerColor: Color = Color(224, 224, 228, 255)

    val targetAchievedColor: Color = Color(0xFF00C980)
    val partialTargetAchievedColor: Color = Color(0xFFFDD835)
    val noAchievementColor: Color = Color(0xFFFF4646)
    val carbohydratesColor: Color = Color(191, 160, 255, 255)

    val primaryTextColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) Black else LightGray

    val scaffoldContentColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) DarkGray else LightGray

    val scaffoldBackgroundColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) Color.White else primaryBlack

    val cardBackgroundColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) LightGray else primaryDarkColor

    val statusBarColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) Color.White else primaryBlack

    val dividerColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) DarkGray else DarkGray
}