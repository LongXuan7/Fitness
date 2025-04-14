package com.example.fitness.data.repository

import com.example.fitness.data.model.Category
import com.example.fitness.util.base.BaseFirebaseRepository
import com.google.firebase.database.DataSnapshot

class CategoryRepository(override val nodeName: String) : BaseFirebaseRepository<Category>() {
    override fun parseSnapshot(snapshot: DataSnapshot): Category? {
        return snapshot.getValue(Category::class.java)
    }
}