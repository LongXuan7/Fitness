package com.example.fitness.ui.reminder

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Reminder
import com.example.fitness.data.repository.ReminderRepository
import com.example.fitness.util.base.BaseViewModel
import java.util.Locale

class ReminderViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val reminderRepository = ReminderRepository("reminder")
    private val reminderRepositoryEn = ReminderRepository("reminder_en")

    init {
        getAllReminders()
    }

    private val _reminderList = MutableLiveData<List<Reminder>>()
    val reminderList: LiveData<List<Reminder>> = _reminderList

    private fun getAllReminders() {
        launchWithErrorHandling(
            block = {
                if (currentLanguage == "en") {
                    reminderRepositoryEn
                } else {
                    reminderRepository
                }.getAll(
                    onResult = { reminders ->
                        _reminderList.postValue(reminders)
                    },
                    onError = {
                        throw Exception(it)
                    }
               )
            },
            onError = {
                throw Exception(it)
            }
        )
    }

    private val _deleteReminder = MutableLiveData(false)
    val deleteReminder: LiveData<Boolean> = _deleteReminder

    fun deleteReminder(id : String) {
        launchWithErrorHandling(
            block = {
                reminderRepository.delete(
                    id,
                    onComplete = {
                       if (currentLanguage == "vi") {
                           _deleteReminder.postValue(true)
                       }
                    }
                )
                reminderRepositoryEn.delete(
                    id,
                    onComplete = {
                        if (currentLanguage == "en") {
                            _deleteReminder.postValue(true)
                        }
                    }
                )
            },
            onError = {
                throw Exception(it)
            }
        )
    }
}