package com.example.fitness.data.repository

import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Sport
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class SportRepository(override val nodeName: String) : BaseFirebaseRepository<Sport>() {
    override fun parseSnapshot(snapshot: DataSnapshot): Sport? {
        return snapshot.getValue(Sport::class.java)
    }
}