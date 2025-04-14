package com.example.fitness.data.repository

import android.util.Log
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class MyNutritionRepository(override val nodeName: String) : BaseFirebaseRepository<MyNutrition>() {
    override fun parseSnapshot(snapshot: DataSnapshot): MyNutrition? {
        return snapshot.getValue(MyNutrition::class.java)
    }
}