package com.sparshchadha.workout_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDtoItem
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.domain.repository.WorkoutRepository
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.CategoryType
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "WorkoutViewModel"

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
) : ViewModel() {
    private var _difficultyForYoga = mutableStateOf(DifficultyLevel.BEGINNER)

    private var _yogaPosesFromApi = mutableStateOf<YogaPosesDto?>(null)
    val yogaPosesFromApi = _yogaPosesFromApi

    private var _gymExercisesFromApi = mutableStateOf<GymExercisesDto?>(null)
    val gymExercisesFromApi = _gymExercisesFromApi

    private var _selectedCategoryForGymExercise = mutableStateOf(CategoryType.WORKOUT_TYPE)


    private val _uiEventState = MutableStateFlow<UIEvent?>(value = null)
    val uiEventStateFlow = _uiEventState.asStateFlow()

    private var _yogaPosesPerformed = mutableStateOf<List<YogaEntity>?>(null)
    val yogaPosesPerformed = _yogaPosesPerformed

    private var _gymExercisesPerformed = mutableStateOf<List<GymExercisesEntity>?>(null)
    val gymExercisesPerformed = _gymExercisesPerformed

    private val _yogaPosesPerformedOnUIEventState = MutableStateFlow<UIEvent?>(UIEvent.ShowLoader)
    val yogaPosesPerformedOnUIEventState = _yogaPosesPerformedOnUIEventState.asStateFlow()

    private val _selectedDateAndMonthForYogaPoses = MutableStateFlow<Pair<Int, String>?>(null)
    val selectedDateAndMonthForYogaPoses = _selectedDateAndMonthForYogaPoses.asStateFlow()

    private val _gymExercisesPerformedOnUIEventState =
        MutableStateFlow<UIEvent?>(UIEvent.ShowLoader)
    val gymExercisesPerformedOnUIEventState = _gymExercisesPerformedOnUIEventState.asStateFlow()

    private val _selectedDateAndMonthForGymExercises = MutableStateFlow<Pair<Int, String>?>(null)
    val selectedDateAndMonthForGymExercises = _selectedDateAndMonthForGymExercises.asStateFlow()

    private val _exerciseDetails = mutableStateOf<GymExercisesDtoItem?>(null)
    val exerciseDetails = _exerciseDetails

    fun getYogaPosesFromApi() {
        viewModelScope.launch {
            val poses =
                workoutRepository.getYogaPosesByDifficultyFromApi(difficulty = _difficultyForYoga.value)

            poses.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _yogaPosesFromApi.value = response.data
                        _uiEventState.emit(
                            UIEvent.HideLoaderAndShowResponse
                        )
                    }

                    is Resource.Loading -> {
                        _uiEventState.emit(
                            UIEvent.ShowLoader
                        )
                    }

                    is Resource.Error -> {
                        _uiEventState.emit(
                            UIEvent.ShowError(
                                errorMessage = "Error - ${response.error?.message}"
                            )
                        )
                    }
                }
            }
        }
    }

    fun getExercisesByMuscleFromApi(muscleType: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByMuscleFromApi(muscleType = muscleType)

            exercises.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _gymExercisesFromApi.value = response.data
                        _uiEventState.emit(
                            UIEvent.HideLoaderAndShowResponse
                        )
                    }

                    is Resource.Loading -> {
                        _uiEventState.emit(
                            UIEvent.ShowLoader
                        )
                    }

                    is Resource.Error -> {
                        _uiEventState.emit(
                            UIEvent.ShowError(
                                errorMessage = "Error - ${response.error?.message}"
                            )
                        )
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
                        _gymExercisesFromApi.value = response.data
                        _uiEventState.emit(
                            UIEvent.HideLoaderAndShowResponse
                        )
                    }

                    is Resource.Loading -> {
                        _uiEventState.emit(
                            UIEvent.ShowLoader
                        )
                    }

                    is Resource.Error -> {
                        _uiEventState.emit(
                            UIEvent.ShowError(
                                errorMessage = "Error - ${response.error?.message}"
                            )
                        )
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
                        _gymExercisesFromApi.value = response.data
                        _uiEventState.emit(
                            UIEvent.HideLoaderAndShowResponse
                        )
                    }

                    is Resource.Loading -> {
                        _uiEventState.emit(
                            UIEvent.ShowLoader
                        )
                    }

                    is Resource.Error -> {
                        _uiEventState.emit(
                            UIEvent.ShowError(
                                errorMessage = "Error - ${response.error?.message}"
                            )
                        )
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
                        _gymExercisesFromApi.value = response.data
                        _uiEventState.emit(
                            UIEvent.HideLoaderAndShowResponse
                        )
                    }

                    is Resource.Loading -> {
                        _uiEventState.emit(
                            UIEvent.ShowLoader
                        )
                    }

                    is Resource.Error -> {
                        _uiEventState.emit(
                            UIEvent.ShowError(
                                errorMessage = "Error - ${response.error?.message}"
                            )
                        )
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

    fun saveYogaPose(yogaPose: YogaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveYogaPose(yogaPose = yogaPose)
        }
    }

    fun getAllPerformedYogaPoses() {
        viewModelScope.launch(Dispatchers.IO) {
            val poses = workoutRepository.getAllYogaPosesPerformed()
            poses.collect { response ->
                withContext(Dispatchers.Main) {
                    _yogaPosesPerformed.value = response
                }
                _uiEventState.emit(UIEvent.HideLoaderAndShowResponse)
            }
        }
    }

    fun getYogaPosesPerformedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val poses = workoutRepository.getYogaPosesPerformedOn(date, month)
            poses.collect { response ->
                withContext(Dispatchers.Main) {
                    _yogaPosesPerformed.value = response
                }
                _yogaPosesPerformedOnUIEventState.emit(UIEvent.HideLoaderAndShowResponse)
                _selectedDateAndMonthForYogaPoses.emit(Pair(date.toInt(), month))
            }
        }
    }

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
                _gymExercisesPerformedOnUIEventState.emit(UIEvent.HideLoaderAndShowResponse)
            }
        }
    }

    fun saveGymExercise(gymExercisesEntity: GymExercisesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveGymExercise(gymExercisesEntity = gymExercisesEntity)
        }
    }

    fun updateExerciseDetails(exercisesDtoItem: GymExercisesDtoItem) {
        _exerciseDetails.value = exercisesDtoItem
    }

    fun removeYogaPose(yogaEntity: YogaEntity) {
        viewModelScope.launch (Dispatchers.IO) {
            workoutRepository.removeYogaPose(yogaEntity)
        }
    }

    fun removeGymExercise(exercisesEntity: GymExercisesEntity) {
        viewModelScope.launch (Dispatchers.IO) {
            workoutRepository.removeGymExercise(exercisesEntity)
        }
    }

    sealed class UIEvent {
        data class ShowError(val errorMessage: String) : UIEvent()
        object HideLoaderAndShowResponse : UIEvent()
        object ShowLoader : UIEvent()
    }
}