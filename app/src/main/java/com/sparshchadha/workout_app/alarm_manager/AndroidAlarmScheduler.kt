package com.sparshchadha.workout_app.alarm_manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.sparshchadha.workout_app.domain.repository.RemindersRepository
import com.sparshchadha.workout_app.util.Constants.REMINDER_DESCRIPTION_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

private const val TAG = "CurrentTagggggg"

class AndroidAlarmScheduler(
    private val context: Context,
    private val remindersRepository: RemindersRepository
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val ALARM_THRESHOLD_MINUTES = 2

    @SuppressLint("ScheduleExactAlarm")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra(REMINDER_DESCRIPTION_KEY, item.description)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun scheduleAlarmsPostReboot() {
        CoroutineScope(Dispatchers.IO).launch {
            val reminders = remindersRepository.getAllReminders()
            Log.e(TAG, "Reminders - $reminders")
            reminders.forEach { reminderEntity ->
                scheduleNew(
                    item = AlarmItem(
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
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNew(item: AlarmItem) {
        val currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        val alarmTime = item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

        // Calculate the difference between the current time and the alarm time in minutes
        val differenceMinutes = Duration.between(
            Instant.ofEpochMilli(currentTime),
            Instant.ofEpochMilli(alarmTime)
        ).toMinutes()

        // If the difference is within the threshold, schedule the alarm for the current time plus the threshold
        val scheduledTime =
            if (differenceMinutes <= ALARM_THRESHOLD_MINUTES) currentTime
            else alarmTime

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra(REMINDER_DESCRIPTION_KEY, item.description)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            scheduledTime,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}