package com.example.fitness.ui.add_reminder

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Reminder
import com.example.fitness.data.repository.ReminderRepository
import com.example.fitness.util.base.BaseViewModel

class AddReminderViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val reminderRepository = ReminderRepository("reminder")
    private val reminderRepositoryEn = ReminderRepository("reminder_en")

    private val _addReminder = MutableLiveData(false)
    val addReminder: LiveData<Boolean> = _addReminder

    fun addReminder(reminder: Reminder) {
        launchWithErrorHandling(
            block = {
                reminderRepository.add(
                    reminder.id.toString(),
                    reminder,
                    onComplete = {
                        if (currentLanguage == "vi") {
                            _addReminder.value = it
                        }
                    },
                )
                reminderRepositoryEn.add(
                    reminder.id.toString(),
                    reminder,
                    onComplete = {
                        if (currentLanguage == "en") {
                            _addReminder.value = it
                        }
                    },
                )
            },
            onError = {
                throw Exception(it)
            }
        )
    }

    private val _updateReminder = MutableLiveData(false)
    val updateReminder: LiveData<Boolean> = _updateReminder

    fun updateReminder(reminder: Reminder) {
        launchWithErrorHandling(
            block = {
                reminderRepository.update(
                    reminder.id.toString(),
                    reminder.toMap(),
                    onComplete = {
                        if (currentLanguage == "vi") {
                            _updateReminder.value = it
                        }
                    },
                )
                reminderRepositoryEn.update(
                    reminder.id.toString(),
                    reminder.toMap(),
                    onComplete = {
                        if (currentLanguage == "en") {
                            _updateReminder.value = it
                        }
                    },
                )
            },
            onError = {
                throw Exception(it)
            }
        )
    }
}