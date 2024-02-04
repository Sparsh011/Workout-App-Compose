package com.sparshchadha.workout_app.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

object Dimensions {
    val TITLE_SIZE
    @Composable
    get() = 22.nonScaledSp

    val ACHIEVEMENT_INDICATOR_COLOR_SIZE: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.indicator_color_size)

    val LARGE_PADDING: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.large_padding)

    val MEDIUM_PADDING: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.medium_padding)

    val SMALL_PADDING: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.small_padding)

    val PIE_CHART_SIZE: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.pie_chart_size)

    val LOTTIE_ANIMATION_SIZE_LARGE: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.lottie_animation_size_large)

    val HEADING_SIZE: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.heading_size)

    val MACRONUTRIENT_TEXT_HEIGHT: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.macronutrient_text_height)

    val CIRCLE_STRIPE_WIDTH: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.macronutrient_text_height)
}
