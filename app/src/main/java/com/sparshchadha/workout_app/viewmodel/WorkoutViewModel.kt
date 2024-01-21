package com.sparshchadha.workout_app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.domain.repository.WorkoutRepository
import com.sparshchadha.workout_app.ui.screens.workout.gym.CategoryType
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "WorkoutViewModel"

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
) : ViewModel() {
    private var _difficultyForYoga = mutableStateOf(DifficultyLevel.BEGINNER)

    private var _yogaPoses = mutableStateOf<YogaPosesDto?>(null)
    val yogaPoses = _yogaPoses

    private var _exercises = mutableStateOf<GymWorkoutsDto?>(null)
    val exercises = _exercises

    private var _selectedCategoryForGymExercise = mutableStateOf(CategoryType.WORKOUT_TYPE)

    var showErrorToast = mutableStateOf(false)
    var showErrorMessageInToast = mutableStateOf("")

    fun getYogaPoses() {
        viewModelScope.launch {
            val poses = workoutRepository.getYogaPosesByDifficulty(difficulty = _difficultyForYoga.value)

            poses.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _yogaPoses.value = response.data
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {
                        showErrorToast.value = true
                        showErrorMessageInToast.value = response.error?.message.toString()
                    }
                }
            }
        }
    }

    fun getExercisesByMuscle(muscleType: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByMuscle(muscleType = muscleType)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _exercises.value = response.data
                    }

                    else -> {
                        Log.e(TAG, "getYogaPoses: Unable To Get Poses!")
                    }
                }
            }
        }
    }

    fun getExercisesByWorkoutType(workoutType: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByWorkoutType(workoutType = workoutType)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _exercises.value = response.data
                    }

                    else -> {
                        Log.e(TAG, "getYogaPoses: Unable To Get Poses!")
                    }
                }
            }
        }
    }

    fun getExercisesByDifficulty(difficultyLevel: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByDifficultyLevel(difficulty = difficultyLevel)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _exercises.value = response.data
                    }

                    else -> {
                        Log.e(TAG, "getYogaPoses: Unable To Get Poses!")
                    }
                }
            }
        }
    }

    fun getExercisesByName(searchQuery: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExerciseByName(name = searchQuery)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _exercises.value = response.data
                    }

                    else -> {
                        Log.e(TAG, "getYogaPoses: Unable To Get Poses!")
                    }
                }
            }
        }
    }

    fun updateYogaDifficultyLevel(difficultyLevel: DifficultyLevel) {
        _difficultyForYoga.value = difficultyLevel
    }

    fun getCurrentYogaDifficultyLevel(): String = _difficultyForYoga.value.name

    fun updateCategoryTypeForGymWorkout(categoryType: CategoryType) {
        _selectedCategoryForGymExercise.value = categoryType
    }

    fun getCurrentCategoryTypeForGymWorkout(): CategoryType = _selectedCategoryForGymExercise.value
}