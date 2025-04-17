package com.example.fitness.ui.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Reminder
import com.example.fitness.data.repository.ReminderRepository
import com.example.fitness.util.base.BaseViewModel

class ReminderViewModel : BaseViewModel() {
    private val reminderRepository = ReminderRepository("reminder")

    init {
        getAllReminders()
    }

    private val _reminderList = MutableLiveData<List<Reminder>>()
    val reminderList: LiveData<List<Reminder>> = _reminderList

    private fun getAllReminders() {
        launchWithErrorHandling(
            block = {
               reminderRepository.getAll(
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
                        _deleteReminder.postValue(true)
                    }
                )
            },
            onError = {
                throw Exception(it)
            }
        )
    }
}