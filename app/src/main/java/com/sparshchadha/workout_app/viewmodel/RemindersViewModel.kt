package com.sparshchadha.workout_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.local.room_db.entities.ReminderEntity
import com.sparshchadha.workout_app.domain.repository.RemindersRepository
import com.sparshchadha.workout_app.ui.screens.reminders.ReminderTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val remindersRepository: RemindersRepository,
) : ViewModel() {

    private val _reminderAdded = MutableStateFlow(false)
    val reminderAdded = _reminderAdded.asStateFlow()

    private val _reminderUpdated = MutableStateFlow(false)
    val reminderUpdated = _reminderUpdated.asStateFlow()

    private val _foodReminders = MutableStateFlow<List<ReminderEntity>?>(null)
    val foodReminders = _foodReminders.asStateFlow()

    private val _workoutReminders = MutableStateFlow<List<ReminderEntity>?>(null)
    val workoutReminders = _workoutReminders.asStateFlow()

    fun addReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val addReminder = remindersRepository.addReminder(reminderEntity = reminderEntity)
                if (addReminder.toInt() != -1) {
                    _reminderAdded.emit(true)
                } else {
                    _reminderAdded.emit(false)
                }
            } catch (e: Exception) {
                _reminderAdded.emit(false)
            }
        }
    }


    fun updateReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updateReminder = remindersRepository.updateReminder(reminderEntity = reminderEntity)
                if (updateReminder != -1) {
                    _reminderUpdated.emit(true)
                } else {
                    _reminderUpdated.emit(false)
                }
            } catch (e: Exception) {
                _reminderUpdated.emit(false)
            }
        }
    }

    fun getRemindersByReminderType(reminderType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val reminders = remindersRepository.getRemindersByReminderType(reminderType = reminderType)

            reminders.collect {
                if (reminderType == ReminderTypes.FOOD.name) {
                    _foodReminders.emit(it)
                } else {
                    _workoutReminders.emit(it)
                }
            }
        }
    }
}