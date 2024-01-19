package com.sparshchadha.workout_app.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.data.remote.FoodApi
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class FoodRepositoryImpl(
    val api: FoodApi
) : FoodItemsRepository {

    override fun getFoodItems(foodSearchQuery: String): Flow<Resource<NutritionalValueDto>> = flow {
        emit(Resource.Loading())

        try {
            val remoteDishes = api.getNutritionalValue(query = foodSearchQuery, apiKey = BuildConfig.FOOD_API_KEY)
//            val mappedRemoteDishes = mutableListOf<NutritionalValueDto>()
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