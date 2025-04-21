package com.example.fitness.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.fitness.R
import java.util.Random

object NotificationUtils {
    fun showReminderNotification(context: Context, title: String, time: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "reminder_channel"
        val channel =
            NotificationChannel(channelId, "Reminder", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("⏰ $title")
            .setContentText("Đã đến giờ tập luyện")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random().nextInt(), notification)
    }
}
