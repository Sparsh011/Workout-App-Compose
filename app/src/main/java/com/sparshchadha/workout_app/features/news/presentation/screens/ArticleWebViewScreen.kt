package com.sparshchadha.workout_app.features.news.presentation.screens

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
import com.sparshchadha.workout_app.features.news.presentation.viewmodels.NewsViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.shared_ui.components.ui_state.NoInternetScreen
import com.sparshchadha.workout_app.shared_ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.Dimensions

@Composable
fun ArticleWebViewScreen(
    navController: NavController,
    newsViewModel: NewsViewModel,
    globalPaddingValues: PaddingValues,
    sharedViewModel: SharedViewModel
) {
    val articleUrl = newsViewModel.articleToLoadUrl.collectAsStateWithLifecycle().value
    val isConnectedToInternet =
        sharedViewModel.connectedToInternet.collectAsStateWithLifecycle().value ?: false

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "",
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPaddingValues ->
        if (isConnectedToInternet) {
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
                            top = innerPaddingValues.calculateTopPadding()
                        )
                        .fillMaxSize()
                )
            }
        } else {
            NoInternetScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = globalPaddingValues.calculateBottomPadding(),
                        top = innerPaddingValues.calculateTopPadding(),
                        start = Dimensions.LARGE_PADDING,
                        end = Dimensions.LARGE_PADDING
                    )
            )
        }
    }
}