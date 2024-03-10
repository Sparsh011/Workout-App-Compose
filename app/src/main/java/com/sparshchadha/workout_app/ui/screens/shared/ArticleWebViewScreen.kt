package com.sparshchadha.workout_app.ui.screens.shared

import android.webkit.WebView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun ArticleWebViewScreen(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues
) {
    val articleUrl = workoutViewModel.articleToLoadUrl.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "",
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        if (articleUrl.isBlank()) {
            ShowLoadingScreen()
        } else {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        loadUrl(articleUrl)
                    }
                },
                modifier = Modifier
                    .padding(
                        bottom = globalPaddingValues.calculateBottomPadding(),
                        top = paddingValues.calculateTopPadding()
                    )
                    .fillMaxSize()
            )
        }
    }
}