package com.sparshchadha.workout_app.ui.navigation.destinations.shared

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.news.presentation.screens.NewsArticles
import com.sparshchadha.workout_app.features.news.presentation.viewmodels.NewsViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes

fun NavGraphBuilder.newsArticlesComposable(
    navController: NavController,
    newsViewModel: NewsViewModel,
    globalPaddingValues: PaddingValues,
    toggleBottomBarVisibility: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = UtilityScreenRoutes.NewsArticlesScreen.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(false)
        }
        NewsArticles(
            navController = navController,
            newsViewModel = newsViewModel,
            globalPaddingValues = globalPaddingValues,
            sharedViewModel = sharedViewModel
        )
    }
}