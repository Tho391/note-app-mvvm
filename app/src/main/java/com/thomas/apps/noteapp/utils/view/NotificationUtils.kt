package com.thomas.apps.noteapp.utils.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationUtils {
    private fun Context.buildNotification(
        channelId: String,
        content: String,
        title: String,
        category: String?,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        group: String? = null
    ): NotificationCompat.Builder {

        val builder =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(priority)
                .setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText(content)
                )
                .setDefaults(Notification.DEFAULT_SOUND)
        if (category != null) {
            builder.setCategory(category)
        }
        if (group != null) {
            builder.setGroup(group)
            builder.setGroupSummary(true)
            builder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)
        }
        return builder
    }

    private fun Context.createNotificationChannel(
        id: String,
        name: String,
        description: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, importance).apply {
                this.description = description
                this.importance = importance
                this.enableVibration(true)
                this.enableLights(true)
                this.setSound(sound, audioAttributes)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}