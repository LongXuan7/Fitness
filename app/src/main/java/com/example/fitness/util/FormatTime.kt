package com.example.fitness.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
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

    fun mergeDateAndTime(dateStr: String, timeStr: String): String {
        val combined = "$dateStr $timeStr"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH)
        val date = inputFormat.parse(combined)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        return outputFormat.format(date!!)
    }

    fun formatToMonthVietnamese(time: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.time = inputFormat.parse(time)!!
        val month = calendar.get(Calendar.MONTH) + 1
        return "Tháng $month"
    }

    fun formatToYear(time: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
        val date = inputFormat.parse(time)
        return outputFormat.format(date!!)
    }

    fun formatToDayOfWeekEnglish(time: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val date = inputFormat.parse(time)
        return outputFormat.format(date!!)
    }
}