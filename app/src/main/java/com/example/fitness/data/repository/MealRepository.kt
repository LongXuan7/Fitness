package com.example.fitness.data.repository

import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class MealRepository(override val nodeName: String) : BaseFirebaseRepository<Meal>() {
    override fun parseSnapshot(snapshot: DataSnapshot): Meal? {
        return snapshot.getValue(Meal::class.java)
    }
}