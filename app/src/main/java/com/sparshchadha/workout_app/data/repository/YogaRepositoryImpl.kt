package com.sparshchadha.workout_app.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.sparshchadha.workout_app.data.remote.api.YogaApi
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.domain.repository.YogaRepository
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaDifficultyLevels
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class YogaRepositoryImpl (
    val api: YogaApi
) : YogaRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun getYogaPosesByDifficulty(difficulty: YogaDifficultyLevels): Flow<Resource<YogaPosesDto>> = flow {
        emit(Resource.Loading())

        try {
            val remoteDishes = api.getYogaPosesByDifficulty(difficulty = difficulty.name.lowercase())
            emit(Resource.Success(remoteDishes))
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