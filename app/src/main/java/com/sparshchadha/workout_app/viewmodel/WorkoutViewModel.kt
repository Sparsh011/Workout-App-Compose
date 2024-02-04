package com.sparshchadha.workout_app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.local.datastore.WorkoutAppDatastorePreference
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
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
import javax.inject.Inject

private const val TAG = "WorkoutViewModel"

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val datastorePreference: WorkoutAppDatastorePreference,
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

    private val _gymExercisesPerformedOnUIEventState = MutableStateFlow<UIEvent?>(UIEvent.ShowLoader)
    val gymExercisesPerformedOnUIEventState = _gymExercisesPerformedOnUIEventState.asStateFlow()

    private val _selectedDateAndMonthForGymExercises = MutableStateFlow<Pair<Int, String>?>(null)
    val selectedDateAndMonthForGymExercises = _selectedDateAndMonthForGymExercises.asStateFlow()

    fun getYogaPosesFromApi() {
        viewModelScope.launch {
            val poses = workoutRepository.getYogaPosesByDifficulty(difficulty = _difficultyForYoga.value)

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
                        Log.e(TAG, "getYogaPoses: in viewmodel")
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

    fun getExercisesByMuscle(muscleType: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByMuscle(muscleType = muscleType)

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

    fun getExercisesByWorkoutType(workoutType: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByWorkoutType(workoutType = workoutType)

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

    fun getExercisesByDifficulty(difficultyLevel: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExercisesByDifficultyLevel(difficulty = difficultyLevel)

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

    fun getExercisesByName(searchQuery: String) {
        viewModelScope.launch {
            val exercises = workoutRepository.getExerciseByName(name = searchQuery)

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
        viewModelScope.launch {
            workoutRepository.saveYogaPose(yogaPose = yogaPose)
        }
    }

    fun getAllPerformedYogaPoses() {
        viewModelScope.launch(Dispatchers.IO) {
            val poses = workoutRepository.getAllYogaPosesPerformed()
            poses.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _yogaPosesPerformed.value = response.data
                        _uiEventState.value = UIEvent.HideLoaderAndShowResponse
                    }

                    is Resource.Loading -> {
                        _uiEventState.value = UIEvent.ShowLoader
                    }

                    is Resource.Error -> {
                        _uiEventState.value = response.error?.message?.let {
                            UIEvent.ShowError(
                                errorMessage = it
                            )
                        }
                    }
                }
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
                when (response) {
                    is Resource.Success -> {
                        _yogaPosesPerformed.value = response.data
                        _yogaPosesPerformedOnUIEventState.value = UIEvent.HideLoaderAndShowResponse
                        _selectedDateAndMonthForYogaPoses.value = Pair(date.toInt(), month)
                    }

                    is Resource.Loading -> {
                        _yogaPosesPerformedOnUIEventState.value = UIEvent.ShowLoader
                    }

                    is Resource.Error -> {
                        _yogaPosesPerformedOnUIEventState.value = response.error?.message?.let {
                            UIEvent.ShowError(
                                errorMessage = it
                            )
                        }
                    }
                }
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
                when (response) {
                    is Resource.Success -> {
                        _gymExercisesPerformed.value = response.data
                        _selectedDateAndMonthForGymExercises.value = Pair(date.toInt(), month)
                        _gymExercisesPerformedOnUIEventState.value = UIEvent.HideLoaderAndShowResponse
                    }

                    is Resource.Loading -> {
                        _gymExercisesPerformedOnUIEventState.value = UIEvent.ShowLoader
                    }

                    is Resource.Error -> {
                        _gymExercisesPerformedOnUIEventState.value = response.error?.message?.let {
                            UIEvent.ShowError(
                                errorMessage = it
                            )
                        }
                    }
                }
            }
        }
    }

    fun saveGymExercise(gymExercisesEntity: GymExercisesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveGymExercise(gymExercisesEntity = gymExercisesEntity)
        }
    }

    sealed class UIEvent {
        data class ShowError(val errorMessage: String) : UIEvent()
        object HideLoaderAndShowResponse : UIEvent()
        object ShowLoader : UIEvent()
    }
}