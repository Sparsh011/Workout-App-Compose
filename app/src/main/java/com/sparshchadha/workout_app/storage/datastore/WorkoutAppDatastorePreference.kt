package com.sparshchadha.workout_app.storage.datastore

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
            pref[CALORIES_GOAL_KEY] = caloriesGoal ?: ""
        }
    }

    val readCaloriesGoal: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[CALORIES_GOAL_KEY]
        }

    suspend fun saveHeight(height: String?) {
        dataStorePreference.edit { pref ->
            pref[CURRENT_HEIGHT_KEY] = height ?: ""
        }
    }

    val readHeight: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[CURRENT_HEIGHT_KEY]
        }

    suspend fun saveWeightGoal(weightGoal: String?) {
        dataStorePreference.edit { pref ->
            pref[WEIGHT_GOAL_KEY] = weightGoal ?: ""
        }
    }

    val readWeightGoal: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[WEIGHT_GOAL_KEY]
        }

    suspend fun saveCurrentWeight(weight: String?) {
        dataStorePreference.edit { pref ->
            pref[CURRENT_WEIGHT_KEY] = weight ?: ""
        }
    }

    val readCurrentWeight: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[CURRENT_WEIGHT_KEY]
        }

    suspend fun saveAge(age: String?) {
        dataStorePreference.edit { pref ->
            pref[AGE_KEY] = age ?: ""
        }
    }

    val readAge: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[AGE_KEY]
        }

    suspend fun saveGender(gender: String?) {
        dataStorePreference.edit { pref ->
            pref[GENDER_KEY] = gender ?: ""
        }
    }

    val readGender: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[GENDER_KEY]
        }

    suspend fun saveName(name: String?) {
        dataStorePreference.edit { pref ->
            pref[NAME_KEY] = name ?: ""
        }
    }

    val readName: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[NAME_KEY]
        }

    suspend fun saveBase64Image(imageStr: String?) {
        dataStorePreference.edit { pref ->
            pref[IMAGE_URI_KEY] = imageStr ?: ""
        }
    }

    val readBase64ProfilePic: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[IMAGE_URI_KEY]
        }

    suspend fun setDarkMode(setTo: String) {
        dataStorePreference.edit { pref ->
            pref[DARK_MODE_THEME_key] = setTo
        }
    }

    val readDarkMode: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[DARK_MODE_THEME_key]
        }

    suspend fun saveWaterGlassesGoal(goal: String) {
        dataStorePreference.edit { pref ->
            pref[WATER_GLASSES_GOAL_KEY] = goal
        }
    }

    val readWaterGlassesGoal: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[WATER_GLASSES_GOAL_KEY]
        }

    suspend fun saveLoginToken(token: String?) {
        dataStorePreference.edit { pref ->
            pref[LOGIN_TOKEN] = token ?: ""
        }
    }

    val readLoginToken: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[LOGIN_TOKEN]
        }

    suspend fun saveFirstAppOpen(isFirstTimeOpened: String?) {
        dataStorePreference.edit { pref ->
            pref[IS_APP_FIRST_TIME_OPENED] = isFirstTimeOpened ?: "true"
        }
    }

    val readFirstAppOpen: Flow<String?>
        get() = dataStorePreference.data.map { pref ->
            pref[IS_APP_FIRST_TIME_OPENED]
        }

    companion object {
        val CALORIES_GOAL_KEY = stringPreferencesKey("CALORIES_GOAL_KEY")
        val WEIGHT_GOAL_KEY = stringPreferencesKey("WEIGHT_GOAL_KEY")
        val CURRENT_WEIGHT_KEY = stringPreferencesKey("CURRENT_WEIGHT_KEY")
        val CURRENT_HEIGHT_KEY = stringPreferencesKey("CURRENT_HEIGHT_KEY")
        val GENDER_KEY = stringPreferencesKey("GENDER_KEY")
        val AGE_KEY = stringPreferencesKey("AGE_KEY")
        val NAME_KEY = stringPreferencesKey("NAME_KEY")
        val IMAGE_URI_KEY = stringPreferencesKey("IMAGE_URI_KEY")
        val DARK_MODE_THEME_key = stringPreferencesKey("DARK_MODE_THEME_key")
        val WATER_GLASSES_GOAL_KEY = stringPreferencesKey("WATER_GLASSES_GOAL_KEY")
        val LOGIN_TOKEN = stringPreferencesKey("LOGIN_TOKEN")
        val IS_APP_FIRST_TIME_OPENED = stringPreferencesKey("IS_APP_FIRST_TIME_OPENED")
    }

}