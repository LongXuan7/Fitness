package com.example.fitness.data.repository

import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Reminder
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class ReminderRepository(override val nodeName: String) : BaseFirebaseRepository<Reminder>() {
    override fun parseSnapshot(snapshot: DataSnapshot): Reminder? {
        return snapshot.getValue(Reminder::class.java)
    }
}