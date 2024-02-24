package com.sparshchadha.workout_app.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.data.remote.api.PexelsApi
import com.sparshchadha.workout_app.data.remote.dto.pexels.PexelResponseDto
import com.sparshchadha.workout_app.domain.repository.PexelsRepository
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class PexelsRepositoryImpl(
    val api: PexelsApi,
) : PexelsRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun searchImageOnPexel(searchQuery: String): Flow<Resource<PexelResponseDto>> = flow {
        emit(Resource.Loading())

        try {
            val remoteImages = api.searchImageOnPexel(searchQuery = searchQuery, apiKey = BuildConfig.PEXELS_API_KEY)
            emit(Resource.Success(remoteImages))

        } catch (e: HttpException) {
            emit(
                Resource.Error(error = e)
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(error = e)
            )
        }
    }
}