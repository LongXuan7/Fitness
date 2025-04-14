package com.example.fitness.data.repository

import android.util.Log
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.MealPlan
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class MealPlanRepository(override val nodeName: String) : BaseFirebaseRepository<MealPlan>() {
    override fun parseSnapshot(snapshot: DataSnapshot): MealPlan? {
        return snapshot.getValue(MealPlan::class.java)
    }
}