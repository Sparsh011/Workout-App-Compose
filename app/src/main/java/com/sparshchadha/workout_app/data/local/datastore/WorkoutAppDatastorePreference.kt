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

    suspend fun saveHeight(height: String?) {
        dataStorePreference.edit { pref ->
            pref[CURRENT_HEIGHT_KEY] = height!!
        }
    }

    val readHeight: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            null ?: pref[CURRENT_HEIGHT_KEY]
        }

    suspend fun saveWeightGoal(weightGoal: String?) {
        dataStorePreference.edit { pref ->
            pref[WEIGHT_GOAL_KEY] = weightGoal!!
        }
    }

    val readWeightGoal: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            null ?: pref[WEIGHT_GOAL_KEY]
        }

    suspend fun saveCurrentWeight(weight: String?) {
        dataStorePreference.edit { pref ->
            pref[CURRENT_WEIGHT_KEY] = weight!!
        }
    }

    val readCurrentWeight: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            null ?: pref[CURRENT_WEIGHT_KEY]
        }

    suspend fun saveAge(age: String?) {
        dataStorePreference.edit { pref ->
            pref[AGE_KEY] = age!!
        }
    }

    val readAge: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            null ?: pref[AGE_KEY]
        }

    suspend fun saveGender(gender: String?) {
        dataStorePreference.edit { pref ->
            pref[GENDER_KEY] = gender!!
        }
    }

    val readGender: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            null ?: pref[GENDER_KEY]
        }

    companion object {
        val CALORIES_GOAL_KEY = stringPreferencesKey("CALORIES_GOAL_KEY")
        val WEIGHT_GOAL_KEY = stringPreferencesKey("WEIGHT_GOAL_KEY")
        val CURRENT_WEIGHT_KEY = stringPreferencesKey("CURRENT_WEIGHT_KEY")
        val CURRENT_HEIGHT_KEY = stringPreferencesKey("CURRENT_HEIGHT_KEY")
        val GENDER_KEY = stringPreferencesKey("GENDER_KEY")
        val AGE_KEY = stringPreferencesKey("AGE_KEY")
    }

}