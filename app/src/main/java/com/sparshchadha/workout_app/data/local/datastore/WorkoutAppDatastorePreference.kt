package com.sparshchadha.workout_app.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "workout_app_preferences")

class WorkoutAppDatastorePreference @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val dataStorePreference = context.dataStore

    suspend fun saveCaloriesGoal(caloriesGoal: String?) {
        dataStorePreference.edit { pref ->
            pref[CALORIES_GOAL_KEY] = caloriesGoal!!
        }
    }

    val readCaloriesGoal: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            null ?: pref[CALORIES_GOAL_KEY]
        }

    companion object {
        val CALORIES_GOAL_KEY = stringPreferencesKey("CALORIES_GOAL_KEY")
    }

}