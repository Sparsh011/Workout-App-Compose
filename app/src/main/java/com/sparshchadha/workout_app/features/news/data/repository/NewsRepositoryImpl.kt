package com.sparshchadha.workout_app.features.news.data.repository

import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.features.news.data.remote.api.NewsApi
import com.sparshchadha.workout_app.features.news.data.remote.dto.NewsArticlesDto
import com.sparshchadha.workout_app.features.news.domain.repository.NewsRepository
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(
    private val newsApi: NewsApi
) : NewsRepository {
    override suspend fun getNewsArticlesFor(searchQuery: String): Flow<Resource<NewsArticlesDto>> = flow {
        emit(Resource.Loading())

        try {
            val response = newsApi.getNewsFor(searchQuery = searchQuery, apiKey = BuildConfig.NEWS_API_KEY)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                emit(Resource.Error(error = Throwable(message = response.errorBody().toString())))
            }
        } catch (e: Exception) {
            emit(Resource.Error(error = e))
        }
    }
//    }
//        return newsApi.getNewsFor(
//            searchQuery = searchQuery,
//            apiKey = BuildConfig.NEWS_API_KEY
//        )
//    }
}