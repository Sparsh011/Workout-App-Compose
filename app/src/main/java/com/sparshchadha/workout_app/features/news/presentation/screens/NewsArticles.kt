package com.sparshchadha.workout_app.features.news.presentation.screens

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.features.news.data.remote.dto.NewsArticlesDto
import com.sparshchadha.workout_app.features.news.presentation.viewmodels.NewsViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.PullToRefreshLazyColumn
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.ui_state.NoInternetScreen
import com.sparshchadha.workout_app.ui.components.ui_state.NoResultsFoundOrErrorDuringSearch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.Resource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsArticles(
    navController: NavController,
    newsViewModel: NewsViewModel,
    globalPaddingValues: PaddingValues,
    sharedViewModel: SharedViewModel
) {
    val searchQuery = newsViewModel.newsSearchQuery.collectAsState().value
    val isConnectedToInternet =
        sharedViewModel.connectedToInternet.collectAsStateWithLifecycle().value ?: false
    val articlesResponse = newsViewModel.newsArticles.value

    if (isConnectedToInternet && articlesResponse == null) {
        newsViewModel.getNewsArticles(forQuery = searchQuery)
    }
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = articlesResponse) {
        if (isRefreshing) {
            isRefreshing = false
        }
    }

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
        if (isConnectedToInternet || articlesResponse != null) {
            val listState = rememberLazyListState()

            HandleNewsArticlesState(
                articlesResponse = articlesResponse,
                localPaddingValues = localPaddingValues,
                updateArticleToLoadUrl = { url ->
                    newsViewModel.updateArticleToLoad(url = url)
                },
                navigateToWebView = {
                    navController.navigate(UtilityScreenRoutes.ArticleWebViewScreen.route)
                },
                globalPaddingValues = globalPaddingValues,
                listState = listState,
                onRefresh = {
                    isRefreshing = true
                    newsViewModel.getNewsArticles(forQuery = searchQuery)
                },
                isRefreshing = isRefreshing
            )
        } else {
            NoInternetScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        bottom = globalPaddingValues.calculateBottomPadding(),
                        start = Dimensions.LARGE_PADDING,
                        end = Dimensions.LARGE_PADDING
                    )
            )
        }
    }
}

@Composable
fun HandleNewsArticlesState(
    articlesResponse: Resource<NewsArticlesDto>?,
    localPaddingValues: PaddingValues,
    updateArticleToLoadUrl: (String) -> Unit,
    navigateToWebView: () -> Unit,
    globalPaddingValues: PaddingValues,
    listState: LazyListState,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {

    when (articlesResponse) {
        is Resource.Loading -> {
            ShowLoadingScreen()
        }

        is Resource.Success -> {
            val articles = articlesResponse.data?.articles ?: emptyList()
            if (articles.isNotEmpty()) {
                PullToRefreshLazyColumn(
                    items = articles,
                    content = {
                        NewsArticle(
                            title = it.title ?: "NA",
                            urlToImage = it.urlToImage ?: "NA",
                            source = it.source?.name ?: "NA",
                            onClick = {
                                updateArticleToLoadUrl(it.url ?: "")
                                navigateToWebView()
                            }
                        )
                    },
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    lazyColumnModifier = Modifier
                        .padding(top = localPaddingValues.calculateTopPadding())
                        .padding(vertical = MEDIUM_PADDING, horizontal = SMALL_PADDING)
                        .clip(RoundedCornerShape(MEDIUM_PADDING))
                        .background(bottomBarColor)
                )
            } else {
                NoResultsFoundOrErrorDuringSearch(
                    globalPaddingValues = globalPaddingValues,
                    localPaddingValues = localPaddingValues
                )
            }
        }

        is Resource.Error -> {
            NoResultsFoundOrErrorDuringSearch(
                globalPaddingValues = globalPaddingValues,
                localPaddingValues = localPaddingValues,
                message = articlesResponse.error?.message ?: "Unable To Get Articles"
            )
        }

        else -> {}
    }
}

@Composable
private fun NewsArticle(
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


