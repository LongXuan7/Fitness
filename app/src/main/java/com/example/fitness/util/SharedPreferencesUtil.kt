package com.example.fitness.util

import android.content.SharedPreferences

object SharedPreferencesUtil {
    fun getLanguageCode(sharedPref: SharedPreferences): String {
        return sharedPref.getString("language", "vi") ?: "vi"
    }
}