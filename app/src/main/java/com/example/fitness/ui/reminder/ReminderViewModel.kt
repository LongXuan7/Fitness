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
}