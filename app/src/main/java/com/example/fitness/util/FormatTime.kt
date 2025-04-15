package com.example.fitness.util

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object FormatTime {
    @SuppressLint("DefaultLocale")
    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    fun formatDateTime(input: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime = LocalDateTime.parse(input, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.ENGLISH)
        return dateTime.format(outputFormatter)
    }

    fun formatToHourMinute(input: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime = LocalDateTime.parse(input, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        return dateTime.format(outputFormatter)
    }
}