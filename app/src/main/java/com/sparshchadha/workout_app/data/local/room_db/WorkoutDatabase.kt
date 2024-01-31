package com.sparshchadha.workout_app.data.local.room_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sparshchadha.workout_app.data.local.room_db.dao.FoodItemsDao
import com.sparshchadha.workout_app.data.local.room_db.dao.GymExercisesDao
import com.sparshchadha.workout_app.data.local.room_db.dao.YogaDao
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity

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
    abstract fun gymWorkoutsDao(): GymExercisesDao
    abstract fun yogaDao(): YogaDao
    abstract fun foodItemsDao(): FoodItemsDao
}