package com.example.fitness.util.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fitness.util.NotificationUtils

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("title") ?: "Lịch tập"
        val time = intent?.getStringExtra("time") ?: ""

        NotificationUtils.showReminderNotification(context, title, time)
    }
}