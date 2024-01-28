package com.sparshchadha.workout_app.data.local


import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.sparshchadha.workout_app.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDtoItem
import com.sparshchadha.workout_app.data.remote.dto.yoga.Pose
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.util.JsonParser

@ProvidedTypeConverter
class Converters(
     private val jsonParser : JsonParser
) {

    @TypeConverter
    fun fromYogaJson(json: String) : YogaPosesDto? {
        return jsonParser.fromJson<YogaPosesDto>(
            json,
            object : TypeToken<YogaPosesDto>(){}.type
        )
    }

    @TypeConverter
    fun toYogaJson(yogaPose: YogaPosesDto) : String {
        return jsonParser.toJson(
            yogaPose,
            object : TypeToken<YogaPosesDto>(){}.type
        ) ?: ""
    }


    @TypeConverter
    fun fromPoseJson(json: String) : Pose? {
        return jsonParser.fromJson<Pose>(
            json,
            object : TypeToken<Pose>(){}.type
        )
    }

    @TypeConverter
    fun toPoseJson(yogaPose: Pose) : String {
        return jsonParser.toJson(
            yogaPose,
            object : TypeToken<Pose>(){}.type
        ) ?: ""
    }

    @TypeConverter
    fun fromFoodItemJson(json: String) : FoodItem? {
        return jsonParser.fromJson<FoodItem>(
            json,
            object : TypeToken<FoodItem>(){}.type
        )
    }

    @TypeConverter
    fun toFoodItemJson(foodItem: FoodItem) : String {
        return jsonParser.toJson(
            foodItem,
            object : TypeToken<FoodItem>(){}.type
        ) ?: ""
    }


    @TypeConverter
    fun fromGymExerciseJson(json: String) : GymWorkoutsDtoItem? {
        return jsonParser.fromJson<GymWorkoutsDtoItem>(
            json,
            object : TypeToken<GymWorkoutsDtoItem>(){}.type
        )
    }

    @TypeConverter
    fun toFoodItemJson(gymExercise: GymWorkoutsDtoItem) : String {
        return jsonParser.toJson(
            gymExercise,
            object : TypeToken<GymWorkoutsDtoItem>(){}.type
        ) ?: ""
    }
}