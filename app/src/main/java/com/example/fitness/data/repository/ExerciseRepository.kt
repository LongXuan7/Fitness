package com.example.fitness.data.repository

import android.util.Log
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class ExerciseRepository(override val nodeName: String) : BaseFirebaseRepository<Exercise>() {
    override fun parseSnapshot(snapshot: DataSnapshot): Exercise? {
        Log.d("ExerciseRawData", snapshot.value.toString()) // debug
        return snapshot.getValue(Exercise::class.java)
    }
}