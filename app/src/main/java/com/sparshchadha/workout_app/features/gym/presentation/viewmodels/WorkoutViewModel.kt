package com.sparshchadha.workout_app.features.gym.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDtoItem
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.domain.entities.PersonalRecordsEntity
import com.sparshchadha.workout_app.features.gym.domain.repository.PRRepository
import com.sparshchadha.workout_app.features.gym.domain.repository.WorkoutRepository
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.CategoryType
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
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val prRepository: PRRepository
) : ViewModel() {

    private var _gymExercisesFromApi = mutableStateOf<Resource<GymExercisesDto>?>(null)
    val gymExercisesFromApi = _gymExercisesFromApi

    private var _searchExercisesResult = mutableStateOf<Resource<GymExercisesDto>?>(null)
    val searchExercisesResult = _searchExercisesResult

    private var _selectedCategoryForGymExercise = mutableStateOf(CategoryType.WORKOUT_TYPE)

    private var _gymExercisesPerformed = MutableStateFlow<List<GymExercisesEntity>?>(null)
    val gymExercisesPerformed = _gymExercisesPerformed.asStateFlow()

    private val _selectedDateAndMonthForGymExercises = MutableStateFlow<Pair<Int, String>?>(null)
    val selectedDateAndMonthForGymExercises = _selectedDateAndMonthForGymExercises.asStateFlow()

    private val _exerciseDetails = mutableStateOf<GymExercisesDtoItem?>(null)
    val exerciseDetails = _exerciseDetails

    private val _savedExercises = MutableStateFlow<List<GymExercisesEntity>?>(null)
    val savedExercises = _savedExercises.asStateFlow()

    private val _selectedExercise = mutableStateOf<GymExercisesDtoItem?>(null)
    val selectedExercise = _selectedExercise

    private val _personalRecords = MutableStateFlow<List<PersonalRecordsEntity>?>(null)
    val personalRecords = _personalRecords.asStateFlow()

    private val _allExercisesPerformed = MutableStateFlow<List<GymExercisesEntity>?>(null)
    val allExercisesPerformed = _allExercisesPerformed.asStateFlow()

    fun getExercisesByMuscleFromApi(muscleType: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByMuscleFromApi(muscleType = muscleType)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _gymExercisesFromApi.value = response
                    }

                    is Resource.Loading -> {
                        _gymExercisesFromApi.value = Resource.Loading()
                    }

                    is Resource.Error -> {
                        _gymExercisesFromApi.value = Resource.Error(error = response.error!!)
                    }
                }
            }
        }
    }

    fun getExercisesByWorkoutTypeFromApi(workoutType: String) {
        viewModelScope.launch {
            val exercises =
                workoutRepository.getExercisesByWorkoutTypeFromApi(workoutType = workoutType)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _gymExercisesFromApi.value = response
                    }

                    is Resource.Loading -> {
                        _gymExercisesFromApi.value = Resource.Loading()
                    }

                    is Resource.Error -> {
                        _gymExercisesFromApi.value = Resource.Error(error = response.error!!)
                    }
                }
            }
        }
    }

    fun getExercisesByDifficultyFromApi(difficultyLevel: String) {
        viewModelScope.launch {
            val exercises =
                workoutRepository.getExercisesByDifficultyLevelFromApi(difficulty = difficultyLevel)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _gymExercisesFromApi.value = response
                    }

                    is Resource.Loading -> {
                        _gymExercisesFromApi.value = Resource.Loading()
                    }

                    is Resource.Error -> {
                        _gymExercisesFromApi.value = Resource.Error(error = response.error!!)
                    }
                }
            }
        }
    }

    fun getExercisesByNameFromApi(searchQuery: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExerciseByNameFromApi(name = searchQuery)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _searchExercisesResult.value = response
                    }

                    is Resource.Loading -> {
                        _searchExercisesResult.value = Resource.Loading()
                    }

                    is Resource.Error -> {
                        _searchExercisesResult.value = Resource.Error(error = response.error!!)
                    }
                }
            }
        }
    }

    fun updateCategoryTypeForGymWorkout(categoryType: CategoryType) {
        _selectedCategoryForGymExercise.value = categoryType
    }

    fun getCurrentCategoryTypeForGymWorkout(): CategoryType = _selectedCategoryForGymExercise.value

    fun getGymExercisesPerformed(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val exercises = workoutRepository.getGymExercisesPerformedOn(date, month)
            exercises.collect { response ->
                withContext(Dispatchers.Main) {
                    _gymExercisesPerformed.value = response
                }
                _selectedDateAndMonthForGymExercises.emit(Pair(date.toInt(), month))
            }
        }
    }

    fun addGymExerciseToWorkout(gymExercisesEntity: GymExercisesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveExerciseToDB(exercisesEntity = gymExercisesEntity)
        }
    }

    fun updateExerciseDetails(exercisesDtoItem: GymExercisesDtoItem?) {
        _exerciseDetails.value = exercisesDtoItem
    }

    fun addExerciseToSaved(gymExercisesEntity: GymExercisesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveExerciseToDB(exercisesEntity = gymExercisesEntity)
        }
    }

    fun getAllExercisesPerformed() {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.getAllExercises(performed = true).collect {
                _allExercisesPerformed.value = it
            }
        }
    }

    fun getAllPR(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.getAllPR(category = category).collect {
                _personalRecords.value = it
            }
        }
    }

    fun updatePR(personalRecordsEntity: PersonalRecordsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.updatePR(pr = personalRecordsEntity)
        }
    }

    fun addPR(personalRecordsEntity: PersonalRecordsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.addPR(pr = personalRecordsEntity)
        }
    }

    fun deletePR(personalRecordsEntity: PersonalRecordsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.deletePR(pr = personalRecordsEntity)
        }
    }

    fun removeExerciseFromDB(exercise: GymExercisesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.removeGymExercise(exercise)
        }
    }

    fun getSavedExercises() {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.getAllExercises(performed = false).collect {
                _savedExercises.value = it
            }
        }
    }

    fun updateSelectedExercise(selectedExercise: GymExercisesDtoItem?) {
        _selectedExercise.value = selectedExercise
    }

}