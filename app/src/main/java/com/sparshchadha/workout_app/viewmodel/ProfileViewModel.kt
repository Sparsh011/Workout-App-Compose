package com.sparshchadha.workout_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.local.datastore.WorkoutAppDatastorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewModelTagggg"
@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val datastorePreference: WorkoutAppDatastorePreference,
) : ViewModel() {
    val readAge: Flow<String?>
        get() = datastorePreference.readAge

    val readGender: Flow<String?>
        get() = datastorePreference.readGender

    val readHeight: Flow<String?>
        get() = datastorePreference.readHeight

    val readCurrentWeight: Flow<String?>
        get() = datastorePreference.readCurrentWeight

    val readWeightGoal: Flow<String?>
        get() = datastorePreference.readWeightGoal

    val readCaloriesGoal: Flow<String?>
        get() = datastorePreference.readCaloriesGoal

    fun saveAge(age: String) {
        viewModelScope.launch (Dispatchers.IO) {
            datastorePreference.saveAge(age)
        }
    }

    fun saveGender(gender: String) {
        viewModelScope.launch (Dispatchers.IO) {
            datastorePreference.saveGender(gender)
        }
    }

    fun saveHeight(height: String) {
        viewModelScope.launch (Dispatchers.IO) {
            datastorePreference.saveHeight(height)
        }
    }

    fun saveWeight(weight: String) {
        viewModelScope.launch (Dispatchers.IO) {
            datastorePreference.saveCurrentWeight(weight)
        }
    }

    fun saveAllDetails(
        age: String,
        height: String,
        weight: String,
        gender: String,
        weightGoal: String,
        caloriesGoal: String
    ) {
        viewModelScope.launch {
            async {
                datastorePreference.saveAge(age)
            }
            async {
                datastorePreference.saveHeight(height)
            }
            async {
                datastorePreference.saveCurrentWeight(weight)
            }
            async {
                datastorePreference.saveGender(gender)
            }

            async {
                datastorePreference.saveWeightGoal(weightGoal)
            }

            async {
                datastorePreference.saveCaloriesGoal(caloriesGoal)
            }
        }
    }
}