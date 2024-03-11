package com.sparshchadha.workout_app.alarm_manager

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.activity.MainActivity
import com.sparshchadha.workout_app.util.Constants.REMINDER_DESCRIPTION_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "primary_id"
    private val NOTIFICATION_ID = 999

    override fun onReceive(context: Context?, intent: Intent?) {

        val message = intent?.getStringExtra(REMINDER_DESCRIPTION_KEY) ?: return

        context?.let {
            createNotificationChannel(it)
            createNotification(
                context = it,
                notificationMessage = message.substringAfter(';'),
                notificationTitle = "${message.substringBefore(';')} REMINDER!"
            )
        }
    }

    private fun createNotification(
        context: Context,
        notificationMessage: String,
        notificationTitle: String,
    ) {
        val myIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminders"
            val descriptionText = "This channel is used to send reminder notifications."

            val importance = IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}