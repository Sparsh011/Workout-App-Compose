package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel


@Composable
fun NewsArticles(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues
) {
    val searchQuery = workoutViewModel.newsSearchQuery.collectAsState().value
    LaunchedEffect(key1 = Unit) {
        workoutViewModel.getNewsArticles(forQuery = searchQuery)
    }

    val articles = workoutViewModel.newsArticles.value?.articles ?: emptyList()

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "News",
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
        },
        modifier = Modifier
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
            .fillMaxSize(),
        containerColor = scaffoldBackgroundColor
    ) { localPaddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(top = localPaddingValues.calculateTopPadding())
                .padding(vertical = MEDIUM_PADDING, horizontal = SMALL_PADDING)
                .clip(RoundedCornerShape(MEDIUM_PADDING))
                .background(bottomBarColor)
        ) {
            if (articles.isNotEmpty()) {
                items(articles) {
                    NewsArticle(
                        title = it.title ?: "NA",
                        urlToImage = it.urlToImage ?: "NA",
                        source = it.source?.name ?: "NA",
                        onClick = {
                            workoutViewModel.updateArticleToLoad(url = it.url ?: "")
                            navController.navigate(UtilityScreenRoutes.ArticleWebViewScreen.route)
                        }
                    )
                }
            } else {
                item {
                    ShowLoadingScreen()
                }
            }
        }
    }
}

@Composable
fun NewsArticle(
    title: String,
    urlToImage: String,
    source: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MEDIUM_PADDING, vertical = MEDIUM_PADDING),
        colors = CardDefaults.cardColors(
            containerColor = bottomBarColor
        )
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onClick()
                }
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = urlToImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(Dimensions.PIE_CHART_SIZE - LARGE_PADDING)
                        .clip(RoundedCornerShape(MEDIUM_PADDING))
                        .weight(2f),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(MEDIUM_PADDING))

                Column(
                    modifier = Modifier.weight(3f),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 20.nonScaledSp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = SMALL_PADDING),
                        color = ColorsUtil.primaryTextColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Source: $source",
                        style = TextStyle(
                            fontSize = 14.nonScaledSp,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier.padding(top = SMALL_PADDING),
                        color = ColorsUtil.primaryTextColor
                    )
                }
            }
        }
    }
}
