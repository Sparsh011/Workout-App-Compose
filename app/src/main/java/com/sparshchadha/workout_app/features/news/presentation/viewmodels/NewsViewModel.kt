package com.sparshchadha.workout_app.features.news.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.features.news.data.remote.dto.NewsArticlesDto
import com.sparshchadha.workout_app.features.news.domain.repository.NewsRepository
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _newsArticles = mutableStateOf<Resource<NewsArticlesDto>?>(null)
    val newsArticles = _newsArticles

    private val _articleToLoadUrl = MutableStateFlow("")
    val articleToLoadUrl = _articleToLoadUrl.asStateFlow()

    private val _newsSearchQuery = MutableStateFlow("")
    val newsSearchQuery = _newsSearchQuery.asStateFlow()

    fun getNewsArticles(forQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.getNewsArticlesFor(searchQuery = forQuery).collect {
                _newsArticles.value = it
            }
        }
    }

    fun updateArticleToLoad(url: String) {
        _articleToLoadUrl.value = url
    }

    fun updateNewsSearchQuery(query: String) {
        _newsSearchQuery.value = query
    }
}