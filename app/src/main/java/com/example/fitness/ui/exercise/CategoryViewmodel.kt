package com.example.fitness.ui.exercise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.repository.CategoryRepository
import com.example.fitness.data.repository.ExerciseRepository
import com.example.fitness.util.base.BaseViewModel

class CategoryViewmodel : BaseViewModel() {
    private val repository = CategoryRepository("categories")
    private val exerciseRepository = ExerciseRepository("exercise")

    init {
        fetchCategories()
    }

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    fun fetchCategories() {
        launchWithErrorHandling(
            block = {
                repository.getAll(
                    onResult = { list ->
                        _categories.postValue(list)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _exercise = MutableLiveData<List<Exercise>>()
    val exercise: LiveData<List<Exercise>> get() = _exercise

    fun fetchExercise(id : Int?) {
        launchWithErrorHandling(
            block = {
                exerciseRepository.getAll(
                    onResult = { list ->
                        val listExercise = list.filter { it.id == id }
                        Log.d("longnx", "fetchExercise: $listExercise")
                        _exercise.postValue(listExercise)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }
}