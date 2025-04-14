package com.example.fitness.data.repository

import com.example.fitness.data.model.MealCategory
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class MealCategoryRepository(override val nodeName: String) : BaseFirebaseRepository<MealCategory>() {
    override fun parseSnapshot(snapshot: DataSnapshot): MealCategory? {
        return snapshot.getValue(MealCategory::class.java)
    }
}