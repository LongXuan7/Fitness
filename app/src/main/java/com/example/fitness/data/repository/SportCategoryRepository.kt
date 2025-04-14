package com.example.fitness.data.repository

import com.example.fitness.data.model.Category
import com.example.fitness.data.model.SportCategory
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class SportCategoryRepository(override val nodeName: String) : BaseFirebaseRepository<SportCategory>() {
    override fun parseSnapshot(snapshot: DataSnapshot): SportCategory? {
        return snapshot.getValue(SportCategory::class.java)
    }
}