package com.sparshchadha.workout_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sparshchadha.workout_app.data.local.dao.FoodItemsDao
import com.sparshchadha.workout_app.data.local.dao.GymWorkoutsDao
import com.sparshchadha.workout_app.data.local.dao.YogaDao
import com.sparshchadha.workout_app.data.local.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.local.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.entities.YogaEntity

@Database(
    entities = [
        FoodItemEntity::class,
        YogaEntity::class,
        GymExercisesEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class WorkoutAppDatabase : RoomDatabase() {
    abstract fun gymWorkoutsDao(): GymWorkoutsDao
    abstract fun yogaDao(): YogaDao
    abstract fun foodItemsDao(): FoodItemsDao
}