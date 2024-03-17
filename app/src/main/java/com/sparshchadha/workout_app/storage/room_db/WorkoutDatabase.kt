package com.sparshchadha.workout_app.storage.room_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sparshchadha.workout_app.features.food.data.local.room.dao.FoodItemsDao
import com.sparshchadha.workout_app.features.food.data.local.room.dao.WaterDao
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.features.food.domain.entities.WaterEntity
import com.sparshchadha.workout_app.features.gym.data.local.room.dao.GymExercisesDao
import com.sparshchadha.workout_app.features.gym.data.local.room.dao.PRDao
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.domain.entities.PersonalRecordsEntity
import com.sparshchadha.workout_app.features.reminders.data.local.room.dao.RemindersDao
import com.sparshchadha.workout_app.features.reminders.domain.entity.ReminderEntity
import com.sparshchadha.workout_app.features.yoga.data.local.room.dao.YogaDao
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity

@Database(
    entities = [
        FoodItemEntity::class,
        YogaEntity::class,
        GymExercisesEntity::class,
        ReminderEntity::class,
        PersonalRecordsEntity::class,
        WaterEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class WorkoutAppDatabase : RoomDatabase() {
    abstract fun gymWorkoutsDao(): GymExercisesDao
    abstract fun yogaDao(): YogaDao
    abstract fun foodItemsDao(): FoodItemsDao
    abstract fun remindersDao(): RemindersDao
    abstract fun prDao(): PRDao
    abstract fun waterDao(): WaterDao
}