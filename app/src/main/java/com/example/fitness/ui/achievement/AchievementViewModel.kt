package com.example.fitness.ui.achievement

import android.content.SharedPreferences
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


class AchievementViewModel(sharedPref: SharedPreferences) : BaseViewModel(){
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"

    private val workoutPlanRepository = WorkoutPlanRepository(if (currentLanguage == "en") "workout_plan_en" else "workout_plan")
    private val repository = CategoryRepository(if (currentLanguage == "en") "categores_en" else "categories")
    private val exerciseRepository = ExerciseRepository(if (currentLanguage == "en") "exercise_en" else "exercise")

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
                workoutPlanRepository.getAll(
                    onResult = { list ->
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        val filteredList = list.filter { workoutPlan ->
                            workoutPlan.user_id == userId
                        }
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

    private fun fetchExercise() {
        launchWithErrorHandling(
            block = {
                exerciseRepository.getAll(
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
}