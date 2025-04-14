package com.example.fitness.ui.add_exercise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.data.repository.CategoryRepository
import com.example.fitness.data.repository.ExerciseRepository
import com.example.fitness.data.repository.WorkoutPlanRepository
import com.example.fitness.util.base.BaseViewModel
import com.google.gson.Gson

class AddExerciseViewModel : BaseViewModel() {
    private val repository = CategoryRepository("categories")
    private val exerciseRepository = ExerciseRepository("exercise")
    private val workoutPlanRepository = WorkoutPlanRepository("workout_plan")

    init {
        fetchCategories()
    }

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private fun fetchCategories() {
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
                        _exercise.postValue(listExercise)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _updateCountTemp = MutableLiveData(false)
    val updateCountTemp: LiveData<Boolean> get() = _updateCountTemp

    fun updateCountTemp(id: Int?, count: String) {
        launchWithErrorHandling(
            block = {
                repository.update(
                    id.toString(),
                    updates = mapOf(
                        "count_temp" to count.toInt()
                    ),
                   onComplete = { _updateCountTemp.postValue(true) }
                )
            }
        )
    }

    private val _updateAllCountTemp = MutableLiveData(false)
    val updateAllCountTemp: LiveData<Boolean> get() = _updateAllCountTemp

    fun updateAllCountTemp(id: Int?) {
        launchWithErrorHandling(
            block = {
                repository.update(
                    id.toString(),
                    updates = mapOf(
                        "count_temp" to 0
                    ),
                    onComplete = { _updateAllCountTemp.postValue(true) }
                )
            }
        )
    }

    private val _updateCountTemExercise = MutableLiveData(false)
    val updateCountTempExercise: LiveData<Boolean> get() = _updateCountTemExercise

    fun updateCountTempExercise(id: Int?, count: Int) {
        launchWithErrorHandling(
            block = {
                exerciseRepository.update(
                    id.toString(),
                    updates = mapOf(
                        "set_temp" to count
                    ),
                    onComplete = { _updateCountTemExercise.postValue(true) }
                )
            }
        )
    }

    private val _updateAllSetTemp = MutableLiveData(false)
    val updateAllSetTemp: LiveData<Boolean> get() = _updateAllSetTemp

    fun updateAllSetTemp(id: Int?) {
        launchWithErrorHandling(
            block = {
                exerciseRepository.update(
                    id.toString(),
                    updates = mapOf(
                        "set_temp" to 0
                    ),
                    onComplete = { _updateAllSetTemp.postValue(true) }
                )
            }
        )
    }

    private val _addWorkoutPlan = MutableLiveData(false)
    val addWorkoutPlan: LiveData<Boolean> get() = _addWorkoutPlan

    fun addWorkoutPlan(workoutPlan: WorkoutPlan) {
        launchWithErrorHandling(
            block = {
                workoutPlanRepository.add(
                    id = workoutPlan.id.toString(),
                    data = workoutPlan,
                    onComplete = { _addWorkoutPlan.postValue(true) }
                )
            }
        )
    }
}