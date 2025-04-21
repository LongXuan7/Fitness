package com.example.fitness.ui.plan

import android.content.SharedPreferences
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
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class PlanViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val workoutPlanRepository = WorkoutPlanRepository("workout_plan")
    private val workoutPlanRepositoryEn = WorkoutPlanRepository("workout_plan_en")
    private val repository = CategoryRepository("categories")
    private val repositoryEn = CategoryRepository("categores_en")
    private val exerciseRepository = ExerciseRepository("exercise")
    private val exerciseRepositoryEn = ExerciseRepository("exercise_en")

    init {
        getWorkoutPlans()
        fetchCategories()
        fetchExercise()
    }

    private val _workoutPlanList = MutableLiveData<List<WorkoutPlan>>()
    val workoutPlanList: LiveData<List<WorkoutPlan>> get() = _workoutPlanList

    fun getWorkoutPlans() {
       launchWithErrorHandling(
            block = {
                if (currentLanguage == "en") {
                    workoutPlanRepositoryEn
                } else {
                    workoutPlanRepository
                }.getAll(
                    onResult = { list ->
                        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        val filteredList = list.filter { it.user_id == userId }
                        _workoutPlanList.postValue(filteredList)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }

       )
    }

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private fun fetchCategories() {
        launchWithErrorHandling(
            block = {
                if (currentLanguage == "en") {
                    repositoryEn
                } else {
                    repository
                }.getAll(
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

    private fun fetchExercise() {
        launchWithErrorHandling(
            block = {
                if (currentLanguage == "en") {
                    exerciseRepositoryEn
                } else {
                    exerciseRepository
                }.getAll(
                    onResult = { list ->
                        _exercise.postValue(list)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _deleteWorkoutPlan = MutableLiveData(false)
    val deleteWorkoutPlan: LiveData<Boolean> get() = _deleteWorkoutPlan

    fun deleteWorkoutPlan(id: String) {
        launchWithErrorHandling(
            block = {
                workoutPlanRepository.delete(
                    id,
                    onComplete = {
                       if (currentLanguage == "vi") {
                           _deleteWorkoutPlan.postValue(true)
                       }
                    },
                )
                workoutPlanRepositoryEn.delete(
                    id,
                    onComplete = {
                        if (currentLanguage == "en") {
                            _deleteWorkoutPlan.postValue(true)
                        }
                    },
                )
            }
        )
    }
}