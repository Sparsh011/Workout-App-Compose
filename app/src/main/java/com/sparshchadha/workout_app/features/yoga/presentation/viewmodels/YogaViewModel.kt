package com.sparshchadha.workout_app.features.yoga.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.Pose
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.YogaPosesDto
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.features.yoga.domain.repository.YogaRepository
import com.sparshchadha.workout_app.shared_ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class YogaViewModel @Inject constructor(
    private val yogaRepository: YogaRepository
): ViewModel() {
    private var _difficultyForYoga = mutableStateOf(DifficultyLevel.BEGINNER)

    private var _yogaPosesFromApi = mutableStateOf<Resource<YogaPosesDto>?>(null)
    val yogaPosesFromApi = _yogaPosesFromApi

    private var _yogaPosesPerformed = mutableStateOf<List<YogaEntity>?>(null)
    val yogaPosesPerformed = _yogaPosesPerformed

    private val _savedPoses = MutableStateFlow<List<YogaEntity>?>(null)
    val savedPoses = _savedPoses.asStateFlow()

    private val _selectedDateAndMonthForYogaPoses = MutableStateFlow<Pair<Int, String>?>(null)
    val selectedDateAndMonthForYogaPoses = _selectedDateAndMonthForYogaPoses.asStateFlow()

    private val _allYogaPosesPerformed = MutableStateFlow<List<YogaEntity>?>(null)
    val allYogaPosesPerformed = _allYogaPosesPerformed.asStateFlow()

    private val _selectedPose = mutableStateOf<Pose?>(null)
    val selectedPose = _selectedPose

    fun getYogaPosesFromApi() {
        viewModelScope.launch {
            val poses =
                yogaRepository.getYogaPosesByDifficultyFromApi(difficulty = _difficultyForYoga.value)

            poses.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _yogaPosesFromApi.value = response
                    }

                    is Resource.Loading -> {
                        _yogaPosesFromApi.value = Resource.Loading()
                    }

                    is Resource.Error -> {
                        _yogaPosesFromApi.value = Resource.Error(error = response.error!!)
                    }
                }
            }
        }
    }

    fun updateYogaDifficultyLevel(difficultyLevel: DifficultyLevel) {
        _difficultyForYoga.value = difficultyLevel
    }

    fun getCurrentYogaDifficultyLevel(): String = _difficultyForYoga.value.name

    fun saveYogaPose(yogaPose: YogaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            yogaRepository.saveYogaPoseToDB(yogaPose = yogaPose)
        }
    }

    fun getAllYogaPosesPerformed() {
        viewModelScope.launch(Dispatchers.IO) {
            yogaRepository.getAllPoses(performed = true).collect {
                _allYogaPosesPerformed.value = it
            }
        }
    }

    fun getYogaPosesPerformedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val poses = yogaRepository.getYogaPosesPerformedOn(date, month)
            poses.collect { response ->
                withContext(Dispatchers.Main) {
                    _yogaPosesPerformed.value = response
                }
                _selectedDateAndMonthForYogaPoses.emit(Pair(date.toInt(), month))
            }
        }
    }

    fun removeYogaPoseFromDB(yogaEntity: YogaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            yogaRepository.removeYogaPose(yogaEntity)
        }
    }


    fun addYogaPoseToSaved(yogaPose: YogaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            yogaRepository.saveYogaPoseToDB(yogaPose)
        }
    }


    fun getSavedYogaPoses() {
        viewModelScope.launch(Dispatchers.IO) {
            yogaRepository.getAllPoses(performed = false).collect {
                _savedPoses.value = it
            }
        }
    }

    fun updateSelectedYogaPose(selectedPose: Pose?) {
        _selectedPose.value = selectedPose
    }
}