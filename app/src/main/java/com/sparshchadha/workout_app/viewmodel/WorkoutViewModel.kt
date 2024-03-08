package com.sparshchadha.workout_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.PersonalRecordsEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDtoItem
import com.sparshchadha.workout_app.data.remote.dto.yoga.Pose
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.domain.repository.PRRepository
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
    private val prRepository: PRRepository
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

    private var _gymExercisesPerformed = MutableStateFlow<List<GymExercisesEntity>?>(null)
    val gymExercisesPerformed = _gymExercisesPerformed.asStateFlow()

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

    private val _savedExercises = MutableStateFlow<List<GymExercisesEntity>?>(null)
    val savedExercises = _savedExercises.asStateFlow()

    private val _savedPoses = MutableStateFlow<List<YogaEntity>?>(null)
    val savedPoses = _savedPoses.asStateFlow()

    private val _selectedExercise = mutableStateOf<GymExercisesDtoItem?>(null)
    val selectedExercise = _selectedExercise

    private val _selectedPose = mutableStateOf<Pose?>(null)
    val selectedPose = _selectedPose

    private val _personalRecords = MutableStateFlow<List<PersonalRecordsEntity>?>(null)
    val personalRecords = _personalRecords.asStateFlow()

    private val _allExercisesPerformed = MutableStateFlow<List<GymExercisesEntity>?>(null)
    val allExercisesPerformed = _allExercisesPerformed.asStateFlow()

    private val _allYogaPosesPerformed = MutableStateFlow<List<YogaEntity>?>(null)
    val allYogaPosesPerformed = _allYogaPosesPerformed.asStateFlow()

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
            workoutRepository.saveYogaPoseToDB(yogaPose = yogaPose)
        }
    }

    fun getAllYogaPosesPerformed() {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.getAllPoses(performed = true).collect {
                _allYogaPosesPerformed.value = it
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

    fun addGymExerciseToWorkout(gymExercisesEntity: GymExercisesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveExerciseToDB(exercisesEntity = gymExercisesEntity)
        }
    }

    fun updateExerciseDetails(exercisesDtoItem: GymExercisesDtoItem) {
        _exerciseDetails.value = exercisesDtoItem
    }

    fun removeYogaPoseFromDB(yogaEntity: YogaEntity) {
        viewModelScope.launch (Dispatchers.IO) {
            workoutRepository.removeYogaPose(yogaEntity)
        }
    }

    fun removeExerciseFromDB(exercisesEntity: GymExercisesEntity) {
        viewModelScope.launch (Dispatchers.IO) {
            workoutRepository.removeGymExercise(exercisesEntity)
        }
    }

    fun saveExerciseToDB(exercisesEntity: GymExercisesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveExerciseToDB(exercisesEntity =  exercisesEntity)
        }
    }

    fun saveYogaPoseToDB(yogaEntity: YogaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.saveYogaPoseToDB(yogaEntity)
        }
    }

    fun getAllExercisesPerformed() {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.getAllGymExercisesPerformed().collect {
                _allExercisesPerformed.value = it
            }
        }
    }

    fun getSavedExercises() {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.getAllExercises(performed = false).collect {
                _savedExercises.value = it
            }
        }
    }

    fun getSavedYogaPoses() {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.getAllPoses(performed = false).collect {
                _savedPoses.value = it
            }
        }
    }

    fun updateSelectedExercise(selectedExercise: GymExercisesDtoItem?) {
        _selectedExercise.value = selectedExercise
    }

    fun updateSelectedYogaPose(selectedPose: Pose?) {
        _selectedPose.value = selectedPose
    }

    fun getAllPR(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.getAllPR(category = category).collect {
                _personalRecords.value = it
            }
        }
    }

    fun updatePR(pr: PersonalRecordsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.updatePR(pr)
        }
    }

    fun deletePR(pr: PersonalRecordsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.deletePR(pr)
        }
    }

    fun addPR(pr: PersonalRecordsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            prRepository.addPR(pr)
        }
    }

    sealed class UIEvent {
        data class ShowError(val errorMessage: String) : UIEvent()
        object HideLoaderAndShowResponse : UIEvent()
        object ShowLoader : UIEvent()
    }
}