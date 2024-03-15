package com.sparshchadha.workout_app.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.alarm_manager.AlarmItem
import com.sparshchadha.workout_app.alarm_manager.AlarmScheduler
import com.sparshchadha.workout_app.data.local.room_db.entities.ReminderEntity
import com.sparshchadha.workout_app.domain.repository.RemindersRepository
import com.sparshchadha.workout_app.ui.screens.reminders.ReminderTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val remindersRepository: RemindersRepository,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {

    private val _foodReminders = MutableStateFlow<List<ReminderEntity>?>(null)
    val foodReminders = _foodReminders.asStateFlow()

    private val _workoutReminders = MutableStateFlow<List<ReminderEntity>?>(null)
    val workoutReminders = _workoutReminders.asStateFlow()

    private val _remindersUIEvent = MutableStateFlow<RemindersUIEvent?>(null)
    val remindersUIEvent = _remindersUIEvent.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isReminderValid(reminderEntity = reminderEntity)) {

                try {
                    val addReminder =
                        remindersRepository.addReminder(reminderEntity = reminderEntity)
                    if (addReminder.toInt() != -1) {
                        _remindersUIEvent.emit(
                            RemindersUIEvent.ShowToast(message = "Reminder Added")
                        )
                        alarmScheduler.schedule(
                            AlarmItem(
                                description = reminderEntity.reminderType + ";" + reminderEntity.reminderDescription,
                                time = LocalDateTime.of(
                                    reminderEntity.year.toInt(),
                                    reminderEntity.month.toInt(),
                                    reminderEntity.date.toInt(),
                                    reminderEntity.hours,
                                    reminderEntity.minutes
                                )
                            )
                        )
                    } else {
                        _remindersUIEvent.emit(
                            RemindersUIEvent.ShowToast(message = "Unable To Add Reminder")
                        )
                    }
                } catch (e: Exception) {
                    _remindersUIEvent.emit(
                        RemindersUIEvent.ShowToast(message = "Error ${e.message}")
                    )
                }
            } else {
                _remindersUIEvent.emit(
                    RemindersUIEvent.ShowToast(message = "All Fields Must Be Filled")
                )
            }
        }
    }


    fun updateReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isReminderValid(reminderEntity = reminderEntity)) {

                try {
                    val updateReminder =
                        remindersRepository.updateReminder(reminderEntity = reminderEntity)
                    if (updateReminder != -1) {
                        _remindersUIEvent.emit(
                            RemindersUIEvent.ShowToast(message = "Reminder Update")
                        )
                    } else {
                        _remindersUIEvent.emit(
                            RemindersUIEvent.ShowToast(message = "Unable To Update Reminder")
                        )
                    }
                } catch (e: Exception) {
                    _remindersUIEvent.emit(
                        RemindersUIEvent.ShowToast(message = "Error ${e.message}")
                    )
                }
            } else {
                _remindersUIEvent.emit(
                    RemindersUIEvent.ShowToast(message = "All Fields Must Be Filled")
                )
            }
        }
    }

    fun getRemindersByReminderType(reminderType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val reminders =
                remindersRepository.getRemindersByReminderType(reminderType = reminderType)

            reminders.collect {
                if (reminderType == ReminderTypes.FOOD.name) {
                    _foodReminders.emit(it)
                } else {
                    _workoutReminders.emit(it)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                remindersRepository.deleteReminder(reminderEntity = reminderEntity)
                _remindersUIEvent.emit(
                    RemindersUIEvent.ShowToast(message = "Reminder Removed")
                )
                alarmScheduler.cancel(
                    AlarmItem(
                        description = reminderEntity.reminderType + " " + reminderEntity.reminderDescription,
                        time = LocalDateTime.of(
                            reminderEntity.year.toInt(),
                            reminderEntity.month.toInt(),
                            reminderEntity.date.toInt(),
                            reminderEntity.hours,
                            reminderEntity.minutes
                        )
                    )
                )
            } catch (e: Exception) {
                _remindersUIEvent.emit(
                    RemindersUIEvent.ShowToast(message = "Error ${e.message}")
                )
            }
        }
    }

    private fun isReminderValid(reminderEntity: ReminderEntity): Boolean {
        return (
                reminderEntity.month.isNotBlank()
                        && reminderEntity.reminderType.isNotBlank()
                        && reminderEntity.date.isNotBlank() && reminderEntity.reminderDescription.isNotBlank()
                        && reminderEntity.year.isNotBlank()
                )
    }

    fun getAll(){
        viewModelScope.launch {
            remindersRepository
        }
    }

    sealed class RemindersUIEvent {
        data class ShowToast(val message: String) : RemindersUIEvent()
    }
}