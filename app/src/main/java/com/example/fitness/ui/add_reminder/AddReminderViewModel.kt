package com.example.fitness.ui.add_reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Reminder
import com.example.fitness.data.repository.ReminderRepository
import com.example.fitness.util.base.BaseViewModel

class AddReminderViewModel : BaseViewModel() {
    private val reminderRepository = ReminderRepository("reminder")

    private val _addReminder = MutableLiveData(false)
    val addReminder: LiveData<Boolean> = _addReminder

    fun addReminder(reminder: Reminder) {
        launchWithErrorHandling(
            block = {
                reminderRepository.add(
                    reminder.id.toString(),
                    reminder,
                    onComplete = {
                        _addReminder.value = it
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
                        _updateReminder.value = it
                    },
                )
            },
            onError = {
                throw Exception(it)
            }
        )
    }
}