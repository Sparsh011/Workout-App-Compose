package com.sparshchadha.workout_app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.domain.repository.YogaRepository
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaDifficultyLevels
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "WorkoutViewModel"
@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val yogaRepository: YogaRepository
) : ViewModel() {
    private var _difficultyLevel = mutableStateOf(YogaDifficultyLevels.BEGINNER)

    private var _yogaPoses = mutableStateOf<YogaPosesDto?>(null)
    val yogaPoses = _yogaPoses

    fun updateYogaDifficultyLevel(difficultyLevel: YogaDifficultyLevels) {
        _difficultyLevel.value = difficultyLevel
    }

    fun getYogaPoses() {
        viewModelScope.launch {
            val poses = yogaRepository.getYogaPosesByDifficulty(difficulty = _difficultyLevel.value)

            poses.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _yogaPoses.value = response.data
                    }
                    else -> {
                        Log.e(TAG, "getYogaPoses: Unable To Get Poses!")
                    }
                }
            }
        }
    }
}