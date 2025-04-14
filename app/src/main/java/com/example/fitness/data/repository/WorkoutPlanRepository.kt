package com.example.fitness.data.repository

import android.util.Log
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class WorkoutPlanRepository(override val nodeName: String) : BaseFirebaseRepository<WorkoutPlan>() {
    override fun parseSnapshot(snapshot: DataSnapshot): WorkoutPlan? {
        return snapshot.getValue(WorkoutPlan::class.java)
    }
}