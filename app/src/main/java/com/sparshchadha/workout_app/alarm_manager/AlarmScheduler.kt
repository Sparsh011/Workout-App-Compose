package com.sparshchadha.workout_app.alarm_manager

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}