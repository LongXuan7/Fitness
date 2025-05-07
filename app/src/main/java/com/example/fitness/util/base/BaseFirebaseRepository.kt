package com.example.fitness.util.base

import com.example.fitness.data.model.MealItem
import com.example.fitness.data.model.MealPlan
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*

abstract class BaseFirebaseRepository<T> {

    abstract val nodeName: String
    abstract fun parseSnapshot(snapshot: DataSnapshot): T?

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference
        get() = database.getReference(nodeName)

    fun getAll(onResult: (List<T>) -> Unit, onError: (String) -> Unit = {}) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { parseSnapshot(it) }
                onResult(list)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }

    fun getById(id: String, onResult: (List<T>) -> Unit, onError: (String) -> Unit = {}) {
        ref.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { parseSnapshot(it) }
                onResult(list)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }

    fun getMealPlanById(id: String, onResult: (List<MealItem>, List<MealItem>, List<MealItem>) -> Unit, onError: (String) -> Unit = {}) {
        ref.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val breakfastList = mutableListOf<MealItem>()
                val dinnerList = mutableListOf<MealItem>()
                val lunchList = mutableListOf<MealItem>()

                snapshot.child("breakfast").children.forEach { childSnapshot ->
                    val breakfastItem = childSnapshot.getValue(MealItem::class.java)
                    breakfastItem?.let { breakfastList.add(it) }
                }

                snapshot.child("dinner").children.forEach { childSnapshot ->
                    val dinnerItem = childSnapshot.getValue(MealItem::class.java)
                    dinnerItem?.let { dinnerList.add(it) }
                }

                snapshot.child("lunch").children.forEach { childSnapshot ->
                    val lunchItem = childSnapshot.getValue(MealItem::class.java)
                    lunchItem?.let { lunchList.add(it) }
                }

                onResult(breakfastList, lunchList, dinnerList)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }

    fun add(id: String, data: T, onComplete: (Boolean) -> Unit = {}) {
        ref.child(id).setValue(data)
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }

    fun addMealPlan(date: String, mealPlan: MealPlan, onComplete: (Boolean) -> Unit = {}) {
        val updates = mutableListOf<Task<Void>>()

        mealPlan.breakfast?.let { breakfastItems ->
            val task = ref.child(date).child("breakfast").updateChildren(breakfastItems)
            updates.add(task)
        }

        mealPlan.lunch?.let { lunchItems ->
            val task = ref.child(date).child("lunch").updateChildren(lunchItems)
            updates.add(task)
        }

        mealPlan.dinner?.let { dinnerItems ->
            val task = ref.child(date).child("dinner").updateChildren(dinnerItems)
            updates.add(task)
        }

        Tasks.whenAllComplete(updates)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun addBatch(dataMap: Map<String, T>, onComplete: (Boolean) -> Unit = {}) {
        val updates = dataMap.mapValues { it.value }
        ref.updateChildren(updates.mapValues { it.value as Any })
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }

    fun set(id: String, data: T, onComplete: (Boolean) -> Unit = {}) {
        ref.child(id).setValue(data)
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }

    fun update(id: String, updates: Map<String, Any?>, onComplete: (Boolean) -> Unit = {}) {
        ref.child(id).updateChildren(updates)
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }

    fun delete(id: String, onComplete: (Boolean) -> Unit = {}) {
        ref.child(id).removeValue()
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }

    fun deleteMealItem(date: String, mealType: String, nutritionId: String, onComplete: (Boolean) -> Unit = {}) {
        ref.child(date).child(mealType).child(nutritionId)
            .removeValue()
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}